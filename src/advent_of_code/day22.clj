(ns advent-of-code.day22
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(def facing {\> 0, \v 1, \< 2, \^ 3})
(def moves {\> [0 1], \v [1 0], \< [0 -1], \^ [-1 0]})
(def turns {\> [\^ \v], \v [\> \<], \< [\v \^], \^ [\< \>]})

(defn- build [[lines dirs]]
  (let [mmap (mapv #(vec (seq %)) lines)
        row  0
        col  (str/index-of (apply str (first mmap)) ".")
        dirs (map #(if (re-find #"\d+" %)
                     (parse-long %) %)
                  (re-seq #"\d+|[LR]" (first dirs)))]
    {:mmap mmap, :dirs dirs, :start [row col]}))

(defn- get-y [mmap start i x]
  (loop [y start]
    (let [ch (get-in mmap [y x] \space)]
      (cond
        (= ch \space) (recur (+ y i))
        :else         y))))

(defn- wrap [mmap pos dir]
  (let [[y x]  pos
        newpos (case dir
                 ;; First two are easy-ish:
                 \> [y (count (take-while #(= % \space) (mmap y)))]
                 \< [y (dec (count (mmap y)))]
                 \^ [(get-y mmap (dec (count mmap)) -1 x) x]
                 \v [(get-y mmap 0 1 x) x]
                 nil)]
    (if (= \# (get-in mmap newpos)) pos newpos)))

(defn- move [mmap pos dir num]
  (let [mv (moves dir)]
    (loop [step 0, pos pos]
      (cond
        (= step num) pos
        :else        (let [newpos (mapv + mv pos)]
                       (case (get-in mmap newpos \space)
                         \# (recur (inc step) pos)
                         \. (recur (inc step) newpos)
                         (recur (inc step) (wrap mmap pos dir))))))))

(defn- walk [{:keys [mmap dirs start]}]
  (loop [[d & ds] dirs, dir \>, pos start]
    (cond
      (nil? d)    (conj pos (facing dir))
      (number? d) (recur ds dir (move mmap pos dir d))
      :else       (let [i (if (= d "L") 0 1)]
                    (recur ds (get-in turns [dir i]) pos)))))

(defn part-1
  "Day 22 Part 1"
  [input]
  (->> input
       u/to-blocks
       (map u/to-lines)
       build
       walk
       (apply (fn [r c f] (+ (* 1000 (inc r)) (* 4 (inc c)) f)))))

;; This is hard-coded to the specific input for me. Note that this encodes only
;; the sides of the six faces that are needed. The sides are encoded as 0-3 for
;; ESWN on the paper map, keeping with the numerical value of the walker's
;; facing. The same four characters are used to indicate the walker's facing
;; direction.
(def sides-map {[0 1] {\^ [[3 0] 2 \>], \< [[2 0] 2 \>]}
                [0 2] {\^ [[3 0] 1 \^], \> [[2 1] 0 \<], \v [[1 1] 0 \<]}
                [1 1] {\> [[0 2] 1 \^], \< [[2 0] 3 \v]}
                [2 0] {\^ [[1 1] 2 \>], \< [[0 1] 2 \>]}
                [2 1] {\> [[0 2] 0 \<], \v [[3 0] 0 \<]}
                [3 0] {\> [[2 1] 1 \^], \v [[0 2] 3 \v], \< [[0 1] 3 \v]}})

(defn- get-newpos [face side y x dir]
  (let [min-y (* 50 (first face))
        max-y (dec (* 50 (inc (first face))))
        min-x (* 50 (last face))
        max-x (dec (* 50 (inc (last face))))
        pair  [(facing dir) side]]
    (cond
      ;; Right edge to right edge
      (= pair [0 0]) [(- max-y (mod y 50)) max-x]
      ;; Right edge to bottom
      (= pair [0 1]) [max-y (+ min-x (mod y 50))]
      ;; Right edge to left edge
      (= pair [0 2]) [(+ min-y (mod x 50)) min-x]
      ;; Right edge to top
      (= pair [0 3]) [min-y (- max-x (mod y 50))]
      ;; Bottom to right edge
      (= pair [1 0]) [(+ min-y (mod x 50)) max-x]
      ;; Bottom to bottom
      (= pair [1 1]) [max-y (- max-x (mod x 50))]
      ;; Bottom to left edge
      (= pair [1 2]) [(- max-y (mod x 50)) min-x]
      ;; Bottom to top
      (= pair [1 3]) [min-y (+ min-x (mod x 50))]
      ;; Left edge to right edge
      (= pair [2 0]) [(+ min-y (mod y 50)) max-x]
      ;; Left edge to bottom
      (= pair [2 1]) [max-y (- max-x (mod y 50))]
      ;; Left edge to left edge
      (= pair [2 2]) [(- max-y (mod y 50)) min-x]
      ;; Left edge to top
      (= pair [2 3]) [min-y (+ min-x (mod y 50))]
      ;; Top to right edge
      (= pair [3 0]) [(- min-y (mod x 50)) max-x]
      ;; Top to bottom
      (= pair [3 1]) [max-y (+ min-x (mod x 50))]
      ;; Top to left edge
      (= pair [3 2]) [(+ min-y (mod x 50)) min-x]
      ;; Top to top
      (= pair [3 3]) [min-y (- max-x (mod x 50))])))

(defn- wrap-cube [mmap pos dir]
  (let [[y x]             pos
        face              [(quot y 50) (quot x 50)]
        [nface side ndir] (get-in sides-map [face dir])
        npos              (get-newpos nface side y x dir)]
    (if (= \# (get-in mmap npos)) [pos dir] [npos ndir])))

(defn- move-cube [mmap pos dir num]
  (loop [step 0, pos pos, dir dir]
    (cond
      (= step num) [pos dir]
      :else        (let [newpos (mapv + (moves dir) pos)]
                     (case (get-in mmap newpos \space)
                       \# (recur (inc step) pos dir)
                       \. (recur (inc step) newpos dir)
                       (let [[npos ndir] (wrap-cube mmap pos dir)]
                         (recur (inc step) npos ndir)))))))

(defn- walk-cube [{:keys [mmap dirs start]}]
  (loop [[d & ds] dirs, dir \>, pos start]
    (cond
      (nil? d)    (conj pos (facing dir))
      (number? d) (let [[npos ndir] (move-cube mmap pos dir d)]
                    (recur ds ndir npos))
      :else       (let [i (if (= d "L") 0 1)]
                    (recur ds (get-in turns [dir i]) pos)))))

(defn part-2
  "Day 22 Part 2"
  [input]
  (->> input
       u/to-blocks
       (map u/to-lines)
       build
       walk-cube
       (apply (fn [r c f] (+ (* 1000 (inc r)) (* 4 (inc c)) f)))))

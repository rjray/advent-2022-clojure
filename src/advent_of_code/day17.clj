(ns advent-of-code.day17
  (:require [advent-of-code.utils :as u]))

(def shapes
  [[:- [[0 0] [1 0] [2 0] [3 0]]]
   [:+ [#____ [1 2]
        [0 1] [1 1] [2 1]
        #____ [1 0]]]
   [:j [#____ #____ [2 2]
        #____ #____ [2 1]
        [0 0] [1 0] [2 0]]]
   [:i [[0 3]
        [0 2]
        [0 1]
        [0 0]]]
   [:o [[0 1] [1 1]
        [0 0] [1 0]]]])

(defn inside? [[x y]]
  (and (<= 0 x 6)
       (<= 0 y)))

(defn set-block [field [name points]]
  (into field (map vector points (repeat name))))

(defn down [field [name points :as shape]]
  (let [next (map #(update % 1 dec) points)]
    (if (every? (every-pred inside? (comp not field)) next)
      [field [name next]]
      [(set-block field shape) nil])))

(defn side [field blow [name points :as shape]]
  (let [next (map #(update % 0 + blow) points)]
    (if (every? (every-pred inside? (comp not field)) next)
      [name next]
      shape)))

(defn initialize [[name points] height]
  [name (for [[x y] points] [(+ 2 x) (+ 4 height y)])])

(defn drop-shape
  [{:keys [field height gases]} shape]
  (loop [field field
         [blow & gases] gases
         shape (initialize shape height)]
    (let [[field shape] (->> shape (side field blow) (down field))]
      (if shape
        (recur field gases shape)
        {:field field
         :height (->> field keys (map second) (reduce max -1))
         :gases gases}))))

(defn heights [input]
  (let [gases (keep {\> 1 \< -1} input)]
    (->> (cycle shapes)
         (reductions drop-shape {:field {} :height -1 :gases (cycle gases)})
         (map :height))))

(defn part-1
  "Day 17 Part 1"
  [input]
  (inc (nth (heights input) 2022)))

(defn find-cycle [xs]
  (let [cycled (drop 1000 xs)
        length (first
                (for [len (range 5 2000)
                      :let [[a b] (->> cycled
                                       (take (* 2 len))
                                       (split-at len))]
                      :when (= a b)]
                  len))
        start (first
               (for [start (range 1000)
                     :let [[a b] (->> xs
                                      (drop start)
                                      (take (* 2 length))
                                      (split-at length))]
                     :when (= a b)]
                 start))]
    [start length]))

(defn part-2
  "Day 17 Part 2"
  [input]
  (let [dh           (->> (heights input)
                          (partition 2 1)
                          (map (fn [[a b]] (- b a))))
        [start len]  (find-cycle dh)
        cycled       (- 1000000000000 start)
        rotation     (mod cycled len)
        cycles       (quot cycled len)
        init-height  (->> dh (take (+ rotation start)) (reduce + 0))
        cycle-height (->> dh (drop (+ rotation start)) (take len) (reduce + 0))]
    (+ init-height (* cycle-height cycles))))

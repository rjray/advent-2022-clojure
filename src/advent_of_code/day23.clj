(ns advent-of-code.day23
  (:require [advent-of-code.utils :as u]))

(def check {\N [[-1 -1] [-1 0] [-1 1]]
            \S [[1 -1]  [1 0]  [1 1]]
            \W [[-1 -1]  [0 -1]  [1 -1]]
            \E [[-1 1] [0 1] [1 1]]})
(def pick {\N [-1 0], \S [1 0], \W [0 -1], \E [0 1]})
(def alone [[-1 -1] [-1 0] [-1 1]
            [0 -1]  #_____ [0 1]
            [1 -1]  [1 0]  [1 1]])

(defn- sort-pair [a b]
  (let [cmp (compare (first a) (first b))]
    (cond
      (zero? cmp) (compare (last a) (last b))
      :else       cmp)))

(defn- visualize [elves]
  (let [min-x  (apply min (map last elves))
        max-x  (apply max (map last elves))
        min-y  (apply min (map first elves))
        max-y  (apply max (map first elves))
        off    [min-y min-x]
        xwidth (inc (- max-x min-x))
        ywidth (inc (- max-y min-y))
        field  (vec (repeat ywidth (vec (repeat xwidth \.))))
        field  (reduce (fn [f e]
                         (assoc-in f (mapv - e off) \#)) field elves)
        field  (map #(apply str %) field)]
    (do (println "(" min-y "," min-x ") : (" max-y "," max-x ")")
        (println (clojure.string/join "\n" field))
        (println)
        elves)))

(defn- to-map [lines]
  (into #{} (for [y (range (count lines)), x (range (count (first lines)))
                  :when (= \# (get-in lines [y x]))]
              [y x])))

(defn- find-empty [elves]
  (let [min-x (apply min (map last elves))
        max-x (apply max (map last elves))
        min-y (apply min (map first elves))
        max-y (apply max (map first elves))]
    (- (* (inc (- max-y min-y)) (inc (- max-x min-x))) (count elves))))

(defn- alone? [elf elves]
  (let [cnt (count (for [pos alone
                         :let [spot (mapv + pos elf)]
                         :when (elves spot)]
                     spot))]
    (zero? cnt)))

(defn- clear? [elf elves dir]
  (zero? (count (for [pos (check dir)
                      :let [spot (mapv + pos elf)]
                      :when (elves spot)]
                  spot))))

(defn- find-new [elf elves dirs]
  (loop [[dir & dirs] dirs]
    (cond
      (nil? dir)             elf
      (clear? elf elves dir) (mapv + elf (pick dir))
      :else                  (recur dirs))))

(defn- pick-moves [elves dirs]
  (loop [[elf & els] elves, new {}]
    (cond
      (nil? elf)         new
      (alone? elf elves) (recur els (assoc new elf elf))
      :else              (recur els (assoc new elf
                                           (find-new elf elves dirs))))))

(defn- one-round [elves dirs]
  (let [proposed (pick-moves elves dirs)
        clashes  (group-by last proposed)]
    (into #{} (map #(let [move (proposed %)
                          want (count (clashes move))]
                      (if (= 1 want) move %)) elves))))

(defn- iter-rounds [rounds elves]
  (let [dirs (cycle '(\N \S \W \E))]
    (loop [round 0, elves elves, dirs dirs]
      (cond
        (= round rounds) elves
        :else
        (let [new-elves (one-round elves (take 4 dirs))]
          (recur (inc round) new-elves (rest dirs)))))))

(defn part-1
  "Day 23 Part 1"
  [input]
  (->> input
       u/to-lines
       (map seq)
       (mapv vec)
       to-map
       (iter-rounds 10)
       find-empty))

(defn- all-rounds [elves]
  (let [dirs (cycle '(\N \S \W \E))]
    (loop [round 0, elves elves, old #{}, dirs dirs]
      (cond
        (= elves old) round
        :else
        (let [new-elves (one-round elves (take 4 dirs))]
          (recur (inc round) new-elves elves (rest dirs)))))))

(defn part-2
  "Day 23 Part 2"
  [input]
  (->> input
       u/to-lines
       (map seq)
       (mapv vec)
       to-map
       all-rounds))

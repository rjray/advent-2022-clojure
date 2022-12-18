(ns advent-of-code.day18
  (:require [advent-of-code.utils :as u]
            [clojure.math.combinatorics :as comb]))

(def surrounds '((-1 0 0) (1 0 0)
                 (0 -1 0) (0 1 0)
                 (0 0 -1) (0 0 1)))

(defn- faces [blob lava]
  (count (for [s surrounds
               :let [spot (map + blob s)]
               :when (not (lava spot))]
           spot)))

(defn- count-faces [lava]
  (apply + (map #(faces % lava) lava)))

(defn part-1
  "Day 18 Part 1"
  [input]
  (->> input
       u/to-lines
       (map u/parse-out-longs)
       (into #{})
       count-faces))

(defn- find-inner [lava]
  (let [max-x (apply max (map first lava))
        max-y (apply max (map second lava))
        max-z (apply max (map last lava))]
    (loop [frontier #{[0 0 0]}
           inner    (set (for [x (range (inc max-x))
                               y (range (inc max-y))
                               z (range (inc max-z))
                               :let [spot [x y z]]
                               :when (not (lava spot))]
                           spot))]
      (if (empty? frontier)
        inner
        (let [node (first frontier)
              to-visit (for [s surrounds
                             :let [spot (mapv + node s)]
                             :when (and (inner spot)
                                        (not (frontier spot)))]
                         spot)]
          (recur (-> frontier
                     (disj node)
                     (into to-visit))
                 (disj inner node)))))))

(defn- count-faces2 [lava]
  (count-faces (into lava (find-inner lava))))

(defn part-2
  "Day 18 Part 2"
  [input]
  (->> input
       u/to-lines
       (map u/parse-out-longs)
       (into #{})
       count-faces2))

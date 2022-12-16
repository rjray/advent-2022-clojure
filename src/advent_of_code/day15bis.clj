(ns advent-of-code.day15
  (:require [advent-of-code.utils :as u]))

(defn- manhattan-dist [p1 p2]
  (+ (abs (- (first p1) (first p2)))
     (abs (- (last  p1) (last  p2)))))

(defn- get-distances [data sensors]
  (loop [[s & ss] sensors, dists {}]
    (cond
      (nil? s) dists
      :else    (recur ss (assoc dists s (manhattan-dist s (data s)))))))

(defn- get-boundaries [data sensors]
  (let [dists (get-distances data sensors)
        min-x (first (sort (map #(- (first %) (dists %)) sensors)))
        max-x (last (sort (map #(+ (first %) (dists %)) sensors)))
        min-y (first (sort (map #(- (last %) (dists %)) sensors)))
        max-y (last (sort (map #(+ (last %) (dists %)) sensors)))]
    (list min-x max-x min-y max-y dists)))

(defn- build-scan [data]
  (let [sensors                         (set (keys data))
        beacons                         (set (vals data))
        [min-x max-x min-y max-y dists] (get-boundaries data sensors)]
    {:sensors sensors
     :beacons beacons
     :min-x   min-x
     :min-y   min-y
     :max-x   max-x
     :max-y   max-y
     :data    data
     :dists   dists}))

(defn- in-range [pos data dists]
  (reduce (fn [val [sensor beacon]]
            (cond
              (= pos beacon)                  (reduced false)
              (<= (manhattan-dist sensor pos)
                  (dists sensor))             (reduced true)
              :else                           false))
          false data))

(defn- find-along-y [y scan]
  (let [data  (:data scan)
        dists (:dists scan)]
    (for [x (range (:min-x scan) (inc (:max-x scan)))
          :let [pos (list x y)]
          :when (in-range pos data dists)]
      pos)))

(defn part-1
  "Day 15 Part 1"
  [input]
  (->> input
       u/to-lines
       (mapcat #(re-seq #"-?\d+" %))
       (map parse-long)
       (partition 2)
       (partition 2)
       (reduce (fn [col [S B]]
                 (assoc col S B))
               {})
       build-scan
       (find-along-y 2000000)
       ;;(find-along-y 10)
       count))

(defn- calc-frequency [pos]
  (+ (* (first pos) 4000000) (last pos)))

(defn- parse-input [input]
  (->> input
       u/to-lines
       (mapcat #(re-seq #"-?\d+" %))
       (map parse-long)
       (partition 4)
       (map (fn [[x y bx by]]
              (let [dist (manhattan-dist [x y] [bx by])]
                (list x y dist))))))

(defn- sort-pair [a b]
  (let [cmp (compare (first a) (first b))]
    (cond
      (zero? cmp) (compare (last a) (last b))
      :else       cmp)))

(defn- get-ranges [sensors y]
  (for [[sx sy r] sensors
        :let [dy (abs (- y sy)), dx (- r dy)]
        :when (<= dy r)]
    (list (- sx dx) (+ sx dx))))

(defn- solve [sensors y]
  (let [ranges (sort sort-pair (get-ranges sensors y))]
    (loop [[[x1 x2] & rs] (rest ranges), prev-x2 (second (first ranges))]
      (cond
        (nil? x1)      nil
        (> x1 prev-x2) (inc prev-x2)
        :else          (recur rs (max x2 prev-x2))))))

(defn- find-beacon [sensors]
  (loop [[y & ys] (range 4000001)]
    (cond
      (nil? y) nil
      :else    (if-let [x (solve sensors y)]
                 (list x y)
                 (recur ys)))))

(defn part-2
  "Day 15 Part 2"
  [input]
  (->> input
       parse-input
       find-beacon
       calc-frequency))

(ns advent-of-code.day20
  (:require [advent-of-code.utils :as u]))

(defn- insert-at [n x coll]
  (into (into (into [] (take n coll)) [x]) (drop n coll)))

(defn- shift-index [i n data]
  (let [[X & b] (drop-while #(not= i (:pos %)) data)
        a       (take (- n (inc (count b))) data)
        x       (:num X)
        m       (mod (+ x (count a)) (dec n))
        m       (if (zero? m) (dec n) m)]
    (cond
      (zero? x) data
      :else     (insert-at m X (concat a b)))))

(defn- mix [data]
  (let [n (count data)]
    (loop [[i & is] (range n), data data]
      (cond
        (nil? i) data
        :else    (recur is (shift-index i n data))))))

(defn- pull-out [ns base]
  (map #(nth base %) ns))

(defn part-1
  "Day 20 Part 1"
  [input]
  (->> input
       u/to-lines
       (mapv #(hash-map :num (parse-long %2) :pos %1) (iterate inc 0))
       mix
       (map :num)
       cycle
       (drop-while (complement zero?))
       (pull-out [1000 2000 3000])
       (apply +)))

(defn- mix-iters [n data]
  (loop [n n, data data]
    (cond
      (zero? n) data
      :else     (recur (dec n) (mix data)))))

(defn part-2
  "Day 20 Part 2"
  [input]
  (->> input
       u/to-lines
       (mapv #(hash-map :num (* 811589153 (parse-long %2)) :pos %1)
             (iterate inc 0))
       (mix-iters 10)
       (map :num)
       cycle
       (drop-while (complement zero?))
       (pull-out [1000 2000 3000])
       (apply +)))

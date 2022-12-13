(ns advent-of-code.day13
  (:require [clojure.string :as str]
            [clojure.edn :as edn]))

(defn- parse-pairs [input]
  (for [pair (str/split input #"\n\n")]
    (->> pair
         str/split-lines
         (map edn/read-string))))

(defn- evaluate [expr]
  (reduce (fn [_ value] (case value
                          true  (reduced true)
                          false (reduced false)
                          nil))
          nil expr))

(defn- cmp [l r]
  (cond
    (and (number? l) (number? r)) (case (compare l r) -1 true,  +1 false, nil)
    (and (vector? l) (vector? r)) (let [res (evaluate (map cmp l r))]
                                    (cond
                                      (true? res)             true
                                      (false? res)            false
                                      (< (count l) (count r)) true
                                      (> (count l) (count r)) false
                                      :else                   nil))
    (number? l)                   (cmp [l] r)
    (number? r)                   (cmp l [r])))

(defn- is-ordered? [pair]
  (let [[left right] pair]
    (cmp left right)))

(defn- find-ordered [pairs]
  (loop [[pair & pairs] pairs, idx 1, ordered ()]
    (cond
      (nil? pair)        ordered
      (is-ordered? pair) (recur pairs (inc idx) (cons idx ordered))
      :else              (recur pairs (inc idx) ordered))))

(defn part-1
  "Day 13 Part 1"
  [input]
  (->> input
       parse-pairs
       find-ordered
       (reduce +)))

(defn- cmp2 [l r] (if (cmp l r) -1 1))

(defn- get-indices [data]
  (map last (filter #(or (= (first %) [[2]])
                         (= (first %) [[6]]))
                    (map #(list %1 %2) data (iterate inc 1)))))

(defn part-2
  "Day 13 Part 2"
  [input]
  (->> input
       parse-pairs
       (apply concat)
       (concat (list [[2]] [[6]]))
       (sort cmp2)
       get-indices
       (apply *)))

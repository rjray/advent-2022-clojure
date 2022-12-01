(ns advent-of-code.day01
  (:require [clojure.string :as str]))

(defn acc-lines [lines]
  (loop [[line & lines] (str/split-lines lines), acc 0, all ()]
    (cond
      (nil? line) (reverse (cons acc all))
      (= line "") (recur lines 0 (cons acc all))
      :else       (recur lines (+ acc (Integer/parseInt line)) all))))

(defn part-1
  "Day 01 Part 1"
  [input]
  (apply max (acc-lines input)))

(defn part-2
  "Day 01 Part 2"
  [input]
  (->> input
       acc-lines
       sort
       reverse
       (take 3)
       (apply +)))

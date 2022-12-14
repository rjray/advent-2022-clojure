(ns advent-of-code.day14
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

;; The three directions a grain can fall in before being at rest.
(def falls '((0 1) (-1 1) (1 1)))

(defn- make-pairs [line]
  (partition 2 (map parse-long (re-seq #"\d+" line))))

(defn- slope [a b] (map compare b a))

(defn- get-line [a b]
  (let [dir (slope a b)]
    (loop [cur a, line ()]
      (cond
        (= cur b) (reverse (cons cur line))
        :else     (recur (map + cur dir) (cons cur line))))))

(defn- process-line [line]
  (apply concat (for [[a b] (partition 2 1 line)] (get-line a b))))

(defn- make-survey [lines]
  (into {} (map #(vector % "#") (mapcat process-line lines))))

(defn- drop-grain [survey max-y]
  (loop [pos '(500 0)]
    (let [move (first (for [dir falls
                            :let [newpos (map + pos dir)]
                            :when (nil? (survey newpos))]
                        newpos))]
      (cond
        (and (nil? move)
             (= pos '(500 0))) nil
        (nil? move)            pos
        (= max-y (last move))  nil
        :else                  (recur move)))))

(defn- drop-sand [survey]
  (let [max-y (apply max (map last (keys survey)))]
    (loop [survey survey, grain (drop-grain survey max-y)]
      (cond
        (nil? grain) survey
        :else        (let [new-survey (assoc survey grain "o")]
                       (recur new-survey (drop-grain new-survey max-y)))))))

(defn- count-sand [survey]
  (count (get (group-by identity (vals survey)) "o")))

(defn part-1
  "Day 14 Part 1"
  [input]
  (->> input
       u/to-lines
       (map make-pairs)
       make-survey
       drop-sand
       count-sand))

(defn- add-floor [survey]
  (let [max-y (+ (apply max (map last (keys survey))) 2)
        min-x (- (apply min (map first (keys survey))) max-y)
        max-x (+ (apply max (map first (keys survey))) max-y)]
    (into survey (map #(vector % "#")
                      (process-line (list [min-x max-y] [max-x max-y]))))))

(defn part-2
  "Day 14 Part 2"
  [input]
  (->> input
       u/to-lines
       (map make-pairs)
       make-survey
       add-floor
       drop-sand
       count-sand
       inc))

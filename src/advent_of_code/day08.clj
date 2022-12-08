(ns advent-of-code.day08
  (:require [clojure.string :as str]))

(def nmap {\0 0, \1 1, \2 2, \3 3, \4 4, \5 5, \6 6, \7 7, \8 8, \9 9})

(defn- parse-field [input]
  (map #(map nmap (seq %)) (str/split-lines input)))

(defn- at [field y x] (nth (nth field y) x))

(defn- visible? [field x y max-x max-y]
  (let [val (at field y x)]
    (cond
      (or (zero? x) (zero? y) (= x (dec max-x)) (= y (dec max-y)))     true
      (empty? (filter #(>= (at field y %) val) (range x)))             true
      (empty? (filter #(>= (at field y %) val) (range (inc x) max-x))) true
      (empty? (filter #(>= (at field % x) val) (range y)))             true
      (empty? (filter #(>= (at field % x) val) (range (inc y) max-y))) true
      :else                                                            false)))

(defn- count-visible [field]
  (let [size-y (count field)
        size-x (count (first field))]
    (count (for [x (range size-x)
                 y (range size-y)
                 :when (visible? field x y size-x size-y)] [x y]))))

(defn part-1
  "Day 08 Part 1"
  [input]
  (->> input
       parse-field
       count-visible))

(defn- find-count [field ys xs val]
  (cond
    (empty? ys) 0
    (empty? xs) 0
    :else       (loop [[y & ys] ys, [x & xs] xs, sc 0, blocked false]
                  (cond
                    (or blocked
                        (nil? y)) sc
                    :else         (recur ys xs (inc sc)
                                         (>= (at field y x) val))))))

(defn- scenic-score [field y x size-y size-x]
  (let [val  (at field y x)
        yneg (range (dec y) -1 -1)
        ypos (range (inc y) size-y)
        xneg (range (dec x) -1 -1)
        xpos (range (inc x) size-x)]
    (* (find-count field yneg (take (count yneg) (iterate identity x)) val)
       (find-count field ypos (take (count ypos) (iterate identity x)) val)
       (find-count field (take (count xneg) (iterate identity y)) xneg val)
       (find-count field (take (count xpos) (iterate identity y)) xpos val))))

(defn- find-best [field]
  (let [size-y (count field)
        size-x (count (first field))]
    (apply max (for [x (range size-x)
                     y (range size-y)]
                 (scenic-score field y x size-y size-x)))))

(defn part-2
  "Day 08 Part 2"
  [input]
  (->> input
       parse-field
       find-best))

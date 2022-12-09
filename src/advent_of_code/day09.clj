(ns advent-of-code.day09
  (:require [clojure.string :as str]))

(def moves {"U" [1 0]
            "D" [-1 0]
            "L" [0 -1]
            "R" [0 1]})

(defn- dist [ax ay bx by]
  (max (abs (/ (- ax bx) 2)) (abs (/ (- ay by) 2))))

(defn- calc-move [[ax ay] [bx by]]
  (cond
    ;; If still within 1, no movement
    (< (dist ax ay bx by) 1)  [0 0]
    ;; Same column
    (= ax bx)                 [0 (/ (- ay by) 2)]
    ;; Same row
    (= ay by)                 [(/ (- ax bx) 2) 0]
    ;; Upper right
    (and (< by ay) (< bx ax)) [1 1]
    ;; Upper left
    (and (< by ay) (< ax bx)) [-1 1]
    ;; Lower left
    (and (< ay by) (< ax bx)) [-1 -1]
    ;; Lower right
    (and (< ay by) (< bx ax)) [1 -1]
    :else                     nil))

(defn- move-once [rope move]
  (let [[h & t] rope
        curpos  (:pos h)
        newpos  (vec (map + move curpos))
        curseen (:seen h)
        newseen (conj curseen newpos)
        newseg  {:pos newpos, :seen newseen}]
    (cond
      (nil? t) (list newseg)
      :else    (let [newmove (calc-move newpos (:pos (first t)))]
                 (cons newseg (move-once t newmove))))))

(defn- exec-move [rope move]
  (let [[dir num] (str/split move #" ")
        num       (Integer/parseInt num)
        move      (moves dir)]
    (loop [rope rope, num num]
      (cond
        (zero? num) rope
        :else       (recur (move-once rope move) (dec num))))))

(defn- trace-rope [len lines]
  (let [rope (repeat len {:pos [0 0], :seen #{[0 0]}})]
    (reduce exec-move rope lines)))

(defn part-1
  "Day 09 Part 1"
  [input]
  (->> input
       str/split-lines
       (trace-rope 2)
       last
       :seen
       count))

(defn part-2
  "Day 09 Part 2"
  [input]
  (->> input
       str/split-lines
       (trace-rope 10)
       last
       :seen
       count))

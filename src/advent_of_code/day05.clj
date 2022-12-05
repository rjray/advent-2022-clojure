(ns advent-of-code.day05
  (:require [clojure.string :as str]))

(defn parse-state [lines]
  (let [padding "                                        "]
    (apply merge-with concat
           (for [line (reverse lines)]
             (zipmap (range 1 10)
                     (for [[_ c] (partition 4 (str line padding))]
                       (when (not= \space c) [c])))))))

(defn parse-moves [lines]
  (for [line lines
        :let [[_ x y z] (re-matches #"move (\d+) from (\d) to (\d)" line)]]
    {:n (Integer/parseInt x)
     :from (Integer/parseInt y)
     :to (Integer/parseInt z)}))

(defn read-input [input]
  (let [[p1 p2] (str/split input #"\n\n")]
    {:state (parse-state (butlast (str/split-lines p1)))
     :moves (parse-moves (str/split-lines p2))}))

(defn get-answer [state]
  (apply str (for [n (range 1 10)]
               (last (state n)))))

(defn apply-move [state {:keys [n from to]}]
  (-> state
      (update from #(drop-last n %))
      (update to #(concat % (take n (reverse (get state from)))))))

(defn apply-reduce-fn [f game]
  (reduce f (:state game) (:moves game)))

(defn part-1
  "Day 05 Part 1"
  [input]
  (->> input
       read-input
       (apply-reduce-fn apply-move)
       get-answer))

(defn apply-move2 [state {:keys [n from to]}]
  (-> state
      (update from #(drop-last n %))
      (update to #(concat % (reverse (take n (reverse (get state from))))))))

(defn part-2
  "Day 05 Part 2"
  [input]
  (->> input
       read-input
       (apply-reduce-fn apply-move2)
       get-answer))

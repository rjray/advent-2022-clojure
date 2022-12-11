(ns advent-of-code.day11
  (:require [clojure.string :as str]))

(defn- create-fn [line]
  (let [parts      (str/split line #"old ")
        [op value] (str/split (last parts) #" ")
        value      (parse-long value)]
    (if value
      (case op
        "+" (fn [x] (+' value x))
        "*" (fn [x] (*' value x)))
      (fn [x] (*' x x)))))

(defn- parse-monkey [m]
  (let [[mn st op te tt tf] (str/split-lines m)
        monkey              (parse-long (first (re-seq #"\d+" mn)))
        items               (map parse-long (re-seq #"\d+" st))
        func                (create-fn op)
        test                (parse-long (first (re-seq #"\d+" te)))
        if-true             (parse-long (first (re-seq #"\d+" tt)))
        if-false            (parse-long (first (re-seq #"\d+" tf)))]
    {:monkey  monkey
     :handled 0
     :items   (seq items)
     :fn      func
     :test    test
     :true    if-true
     :false   if-false}))

(defn- parse-input [input]
  (vec (map parse-monkey (str/split input #"\n\n"))))

(defn- handle-item [dim item m-idx monkeys]
  (let [monkey   (monkeys m-idx)
        worry    (-> ((:fn monkey) item)
                     dim)
        test     (mod worry (:test monkey))
        target   (if (zero? test) (:true monkey) (:false monkey))
        new_m    (update (update monkey :items rest) :handled inc)
        target_m (monkeys target)]
    (assoc (assoc monkeys m-idx new_m)
           target (assoc target_m :items (concat (:items target_m)
                                                 (list worry))))))

(defn- process-monkey [dim monkey monkeys]
  (loop [[i & is] (:items monkey), monkeys monkeys]
    (cond
      (nil? i) monkeys
      :else    (recur is (handle-item dim i (:monkey monkey) monkeys)))))

(defn- do-one-round [dim monkeys]
  (loop [[m & ms] (range (count monkeys)), monkeys monkeys]
    (cond
      (nil? m) monkeys
      :else    (recur ms (process-monkey dim (monkeys m) monkeys)))))

(defn- run-simulation [rounds dim monkeys]
  (loop [round 1, monkeys monkeys]
    (cond
      (> round rounds) (map :handled monkeys)
      :else            (recur (inc round) (do-one-round dim monkeys)))))

(defn part-1
  "Day 11 Part 1"
  [input]
  (->> input
       parse-input
       (run-simulation 20 #(quot % 3))
       sort
       reverse
       (take 2)
       (apply *)))

(defn- run-simulation2 [rounds monkeys]
  (let [factor (apply * (map :test monkeys))
        dim    #(rem % factor)]
    (loop [round 1, monkeys monkeys]
      (cond
        (> round rounds) (map :handled monkeys)
        :else            (recur (inc round) (do-one-round dim monkeys))))))

(defn part-2
  "Day 11 Part 2"
  [input]
  (->> input
       parse-input
       (run-simulation2 10000)
       sort
       reverse
       (take 2)
       (apply *')))

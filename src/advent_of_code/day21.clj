(ns advent-of-code.day21
  (:require [advent-of-code.utils :as u]))

(def cache-re #"(\w+):\s+(\d+)")
(def exprs-re #"(\w+):\s+(\w+)\s+([-+*/])\s+(\w+)")
(def ops {"+" +, "-" -, "*" *, "/" /})

(defn- to-monkeys [lines]
  (loop [[line & ls] lines, monkeys {}]
    (cond
      (nil? line) monkeys
      :else
      (if-let [[_ name value] (re-find cache-re line)]
        (recur ls (assoc monkeys name {:num (parse-long value)}))
        (let [[_ name left op right] (re-find exprs-re line)]
          (recur ls
                 (assoc monkeys
                        name {:op (ops op), :left left, :right right})))))))

(defn- eval-expr [monkeys node]
  (let [val (monkeys node)]
    (cond
      (:num val) monkeys
      :else      (let [{:keys [op left right]} val
                       values (-> monkeys
                                  (eval-expr left)
                                  (eval-expr right))
                       lval   (get-in values [left :num])
                       rval   (get-in values [right :num])
                       newval (op lval rval)]
                   (assoc monkeys node {:num newval})))))

(defn part-1
  "Day 21 Part 1"
  [input]
  (-> input
      u/to-lines
      to-monkeys
      (eval-expr "root")
      (get-in ["root" :num])))

(defn- extrapolate [monkeys]
  (let [monkeys (assoc-in monkeys ["root" :op] -)
        y0      (get-in (eval-expr (assoc-in monkeys ["humn" :num] 0) "root")
                        ["root" :num])
        y1      (get-in (eval-expr (assoc-in monkeys ["humn" :num] 1) "root")
                        ["root" :num])
        n       (long (/ y0 (- y0 y1)))]
    (if (zero? (get-in (eval-expr (assoc-in monkeys ["humn" :num] n) "root")
                       ["root" :num]))
      n)))

(defn part-2
  "Day 21 Part 2"
  [input]
  (-> input
      u/to-lines
      to-monkeys
      extrapolate))

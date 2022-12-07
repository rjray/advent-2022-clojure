(ns advent-of-code.day07
  (:require [clojure.string :as str]))

(defn- chdir [cwd cmd]
  (let [dir (last (str/split cmd #" "))]
    (if (nil? cwd)
      (list dir)
      (if (= ".." dir)
        (rest cwd)
        (cons dir cwd)))))

(defn- add-dir [state cwd cmd]
  (let [dir    (last (str/split cmd #" "))
        newdir (cons dir cwd)]
    (if (get-in state (reverse newdir))
      state
      (assoc-in state (reverse newdir) {}))))

(defn- add-file [state cwd cmd]
  (let [words (str/split cmd #" ")
        size  (Integer/parseInt (first words))
        fname (last words)]
    (assoc-in state (reverse (cons fname cwd)) size)))

(defn- parse-commands [cmds]
  (loop [[cmd & cmds] cmds, cwd nil, state {"/" {}}]
    (cond
      (nil? cmd)                    state
      (str/starts-with? cmd "$ cd") (recur cmds (chdir cwd cmd) state)
      (str/starts-with? cmd "dir ") (recur cmds cwd (add-dir state cwd cmd))
      (re-find #"^\d" cmd)          (recur cmds cwd (add-file state cwd cmd))
      :else                         (recur cmds cwd state))))

(defn- dirs [cwd state]
  (let [top (get-in state (reverse cwd))]
    (cond
      (map? top) (filter #(map? (top %)) (keys top))
      :else      nil)))

(defn- files [cwd state]
  (let [top (get-in state (reverse cwd))]
    (cond
      (map? top) (filter #(not (map? (top %))) (keys top))
      :else      nil)))

(defn- get-dir-sizes-1 [cwd state sizes]
  (let [dirs-list  (dirs cwd state)
        files-list (files cwd state)]
    (loop [[d & ds] dirs-list, sizes sizes]
      (cond
        (nil? d) (let [size (apply + (map #(get-in state (reverse (cons % cwd)))
                                          files-list))]
                   (assoc sizes cwd (+ size
                                       (apply + (map #(sizes (cons % cwd))
                                                     dirs-list)))))
        :else    (recur ds (get-dir-sizes-1 (cons d cwd) state sizes))))))

(defn- get-dir-sizes [state]
  (get-dir-sizes-1 ["/"] state {}))

(defn part-1
  "Day 07 Part 1"
  [input]
  (->> input
       str/split-lines
       parse-commands
       get-dir-sizes
       vals
       (filter #(<= % 100000))
       (apply +)))

(defn- find-smallest-candidate [sizes]
  (let [unused (- 70000000 (sizes ["/"]))
        needed (- 30000000 unused)]
    (apply min (filter #(>= % needed) (vals sizes)))))

(defn part-2
  "Day 07 Part 2"
  [input]
  (->> input
       str/split-lines
       parse-commands
       get-dir-sizes
       find-smallest-candidate))

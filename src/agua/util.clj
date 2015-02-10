(ns agua.util)

(defmacro with-time-print [expr & body]
  `(let [start# (System/nanoTime)]
     (print (str ~expr "... "))
     (flush)
     (let [result# (do ~@body)]
       (println (str "done. (" (Math/round (/ (unchecked-subtract (System/nanoTime) start#) 1e9)) "s)"))
       result#)))

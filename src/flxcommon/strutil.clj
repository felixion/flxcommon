(ns
  ^{:author "Aaron Evans"
    :doc "String utilities"}
  flxcommon.strutil
  (:import [java.util StringTokenizer]))

(defn- interpolate-next [tokens params]
  (let [[a b c d & rest] tokens]
    (when a
      (cond
        ;; escape out $${foo} sequences
        (= ["$" "$" "{"] [a b c])
        (cons "${" (lazy-seq (interpolate-next (cons d rest) params)))

        ;; perform substitution on ${foo}
        (= ["$" "{" "}"] [a b d])
        (cons (get params c) (lazy-seq (interpolate-next rest params)))

        ;; pass through arbitrary string with no tokens in it
        :default
        (cons a (lazy-seq (interpolate-next (concat [b c d] rest) params)))))))

(defn interpolate
  "Performs keyword-value substitution in a string, by replacing all ${keyword} placeholders
  with the value held by :keyword in a map.

  E.g., (interpolate \"test ${foo}\" :foo \"bar\") is \"test bar\".
  "
  [s & kwargs]
  (when (not (nil? s))
    (apply str (interpolate-next (enumeration-seq (StringTokenizer. s "${}" true)) (apply hash-map kwargs)))))

(interpolate "$$ test")
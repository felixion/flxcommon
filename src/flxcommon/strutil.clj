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

        ;; escape out ::{foo} sequences
        (= [":" ":" "{"] [a b c])
        (cons ":{" (lazy-seq (interpolate-next (cons d rest) params)))

        ;; perform substitution on :{foo}
        (= [":" "{" "}"] [a b d])
        (cons (get params (keyword c)) (lazy-seq (interpolate-next rest params)))

        ;; pass through arbitrary string with no tokens in it
        :default (cons a (lazy-seq (interpolate-next (concat [b c d] rest) params)))))))

(defn interpolate
  "Performs interpolation on a string.  A sequence of named values is inserted over any placeholders are follows:

    ${name} is replaced with the value from \"name\"
    :{name} is replaced with the value from :name
    $${name} and ::{name} are not altered

  Example:
    (interpolate \"foo :{bar} test\" :bar \"baz\")
    ;= \"foo baz test\"
  "
  [s & kwargs]
  (when (not (nil? s))
    (apply str (interpolate-next (enumeration-seq (StringTokenizer. s "$:{}" true)) (apply hash-map kwargs)))))

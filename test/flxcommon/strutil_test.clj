(ns
  ^{:author "Aaron Evans"
    :doc "Unit-tests for flxcommon.strutil"}
  flxcommon.strutil-test
  (:require [clojure.test :refer :all ]
            [flxcommon.strutil :refer :all ]))

(deftest interpolate-test
  (let [tests [["foo ${bar} test" ["bar" "baz"] "foo baz test"]
               ["foo ${bar} test" ["baz" "baz"] "foo  test"]
               ["foo ${bar} test" [] "foo  test"]
               ["foo :{bar} test" [:bar "baz"] "foo baz test"]
               ["foo :{bar} test" [:baz "baz"] "foo  test"]
               ["foo :{bar} test" [] "foo  test"]
               ["${bar} test" ["bar" "baz"] "baz test"]
               ["foo ${bar}" ["bar" "baz"] "foo baz"]
               ["${bar}" ["bar" "baz"] "baz"]
               [":{bar} test" [:bar "baz"] "baz test"]
               ["foo :{bar}" [:bar "baz"] "foo baz"]
               [":{bar}" [:bar "baz"] "baz"]
               ["foo $${bar} test" ["bar" "baz"] "foo ${bar} test"]
               ["$${bar} test" ["bar" "baz"] "${bar} test"]
               ["foo $${bar}" ["bar" "baz"] "foo ${bar}"]
               ["foo ::{bar} test" [:bar "baz"] "foo :{bar} test"]
               ["::{bar} test" [:bar "baz"] ":{bar} test"]
               ["foo ::{bar}" [:bar "baz"] "foo :{bar}"]
               ["foo $ test" ["bar" "baz"] "foo $ test"]
               ["foo $$ test" ["bar" "baz"] "foo $$ test"]
               ["foo $" ["bar" "baz"] "foo $"]
               ["foo $$" ["bar" "baz"] "foo $$"]
               ["$ test" ["bar" "baz"] "$ test"]
               ["$$ test" ["bar" "baz"] "$$ test"]
               ["foo : test" [:bar "baz"] "foo : test"]
               ["foo :: test" [:bar "baz"] "foo :: test"]
               ["foo :" [:bar "baz"] "foo :"]
               ["foo ::" [:bar "baz"] "foo ::"]
               [": test" [:bar "baz"] ": test"]
               [":: test" [:bar "baz"] ":: test"]
               ["foo {bar} test" ["bar" "baz"] "foo {bar} test"]
               ["foo {bar}" ["bar" "baz"] "foo {bar}"]
               ["{bar} test" ["bar" "baz"] "{bar} test"]
               ["foo {{bar}} test" ["bar" "baz"] "foo {{bar}} test"]
               ["foo {{bar}}" ["bar" "baz"] "foo {{bar}}"]
               ["{{bar}} test" ["bar" "baz"] "{{bar}} test"]
               ["" ["bar" "baz"] ""]
               [nil ["bar" "baz"] nil]]]
    ;; for each [s params expected] test that (= (interpolate s params) expected)
    (doseq [[s params expected] tests]
      (is (= expected (apply interpolate s params))))))

(ns
  ^{:author "Aaron Evans"
    :doc "Unit-tests for flxcommon.resources"}
  flxcommon.resources-test
  (:require [clojure.test :refer :all ]
            [flxcommon.resources :refer :all ]))

(deftest resource-test
  (is (nil? (resource "flxcommon/not_a_resource.txt")))
  (with-open [r (resource "flxcommon/resources_test.txt")]
    (= "Resource to load for flxcommon.resources-test" (slurp r))))

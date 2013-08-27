(ns
  ^{:author "Aaron Evans"
    :doc "Unit-tests for flxcommon.logutil"}
  flxcommon.logutil-test
  (:require [clojure.test :refer :all ]
            [flxcommon.logutil :refer :all ])
  (:import [java.util.logging Level Logger LogManager LogRecord]))

(deftest reset-logging-test
  (let [formatter (make-formatter "test - :{message}")
        handler (make-console-handler formatter :level Level/INFO :flush true)
        logger (init-logger "flxcommon.logutil-test.init-logger-test" :level Level/INFO :handler handler)]
    (reset-logging)
    (doseq [loggerName (enumeration-seq (.getLoggerNames (LogManager/getLogManager)))
            :let [logger (Logger/getLogger loggerName)]]
      (if
        (= "" loggerName)
        (is (= Level/INFO (.getLevel logger)))
        (is (nil? (.getLevel logger))))
      (is (= (.getHandlers logger))))))

(deftest make-formatter-test
  (let [formatter (make-formatter ":{loggerName} :{level} :{message}")]
    (is (= "TestLoggerName INFO test logger message"
          (.format formatter
            (doto (LogRecord. Level/INFO "test logger message")
              (.setLoggerName "TestLoggerName")))))))

(deftest make-console-handler-test
  (let [formatter (make-formatter "test - :{message}")
        handler (make-console-handler formatter :level Level/INFO :flush true)]
    (is (= "test - info message\n" (with-out-str (.publish handler (LogRecord. Level/INFO "info message")))))
    (is (= "" (with-out-str (.publish handler (LogRecord. Level/FINE "fine message")))))))

(deftest init-logger-test
  (let [formatter (make-formatter "test - :{message}")
        handler (make-console-handler formatter :level Level/INFO :flush true)
        logger (init-logger "flxcommon.logutil-test.init-logger-test" :level Level/INFO :handler handler)]
    (is (= "flxcommon.logutil-test.init-logger-test" (.getName logger)))
    (is (= Level/INFO (.getLevel logger)))
    (is (= [handler] (seq (.getHandlers logger))))))

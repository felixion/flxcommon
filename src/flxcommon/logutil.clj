(ns
  ^{:author "Aaron Evans"
    :doc "Utilities for configuring java.util.logging"}
  flxcommon.logutil
  (:require [flxcommon.strutil :refer :all ])
  (:import [java.util.logging Formatter Handler Logger LogManager]))

(defn reset-logging
  "Resets the logging system."
  []
  (.reset (LogManager/getLogManager)))

(defn make-formatter
  "Creates a logging Formatter that applies string interpolation between the given
  format string and (bean record)."
  [fmt]
  (proxy [Formatter] []
    (format [record]
      (let [record (bean record)]
        (apply interpolate fmt (apply concat (seq record)))))))

(defn make-console-handler
  "Creates a handler that logs to *out*."
  [formatter & {level :level flush :flush}]
  (let [handler (proxy [Handler] []
                  (publish [record]
                    (when (.isLoggable this record)
                      (print (.format (.getFormatter this) record))
                      (when flush (println))))
                  (flush [] (println))
                  (close []))]
    (.setFormatter handler formatter)
    (when level (.setLevel handler level))
    handler))

(defn init-logger
  "Creates and configures a logger."
  [name & {level :level handler :handler}]
  (doto (Logger/getLogger name)
    (.addHandler handler)
    (.setLevel level)))

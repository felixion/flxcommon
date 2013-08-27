(ns
  ^{:author "Aaron Evans"
    :doc "Helpers for loading resources from the classpath"}
  flxcommon.resources)

(defn resource
  "Return the resource as a stream, or nil when not found."
  [path]
  (let [classloader (.getContextClassLoader (Thread/currentThread))]
    (.getResourceAsStream classloader path)))

(defn resources
  "Returns a stream for each instance of a resource.  The sequence is realized lazily."
  [path]
  (let [classloader (.getContextClassLoader (Thread/currentThread))]
    (for [url (.getResources classloader path)]
      (. url openStream))))

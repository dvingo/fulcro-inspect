(ns fulcro.inspect.remote.transit
  (:require [cognitect.transit :as t]
            [com.cognitect.transit.types :as ty]
            [fulcro.transit :as ft]))

(deftype ErrorHandler []
  Object
  (tag [this v] "js-error")
  (rep [this v] [(ex-message v) (ex-data v)])
  (stringRep [this v] (ex-message v)))

(deftype DefaultHandler []
  Object
  (tag [this v] "unknown")
  (rep [this v] (pr-str v)))

(def write-handlers
  {cljs.core/ExceptionInfo (ErrorHandler.)
   "default"               (DefaultHandler.)})

(def read-handlers
  {"js-error" (fn [[msg data]] (ex-info msg data))
   "unknown" (fn [v] (str "UknownTransitType:" v))})

(defn read [str]
  (let [reader (ft/reader {:handlers read-handlers})]
    (t/read reader str)))

(defn write [x]
  (let [writer (ft/writer {:handlers write-handlers})]
    (t/write writer x)))

(extend-type ty/UUID IUUID)

(defrecord TestIt [a])
(def wr (t/writer :json {:handlers write-handlers}))
(def re (t/reader :json))
(comment
  (t/read re (t/write wr "hi"))
  (t/read re (t/write wr ["hello wordl"]))
  (write (TestIt. 5))
(t/read re (t/write wr (TestIt. 5)))
  (t/read re "[\"~#unknown\",\"#fulcro.inspect.remote.transit.TestIt{:a 5}\"]")

  (read "[\"~#unknown\",\"#fulcro.inspect.remote.transit.TestIt{:a 5}\"]")
 ;; (read (write (TestIt. 5)))

  ;;(write "hello")
  )

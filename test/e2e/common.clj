(ns common
  (:require
   [clojure.test :refer [is testing-contexts-str] :as t]
   [etaoin.api :as eapi]))

(def ^:dynamic *driver* nil)

(defn with-driver
  [f]
  (binding [*driver* (eapi/chrome {:headless true})
            eapi/*wait-timeout* 5]
    (f)
    (eapi/quit *driver*)))

(defn get-severe-logs []
  (filter (comp #{:severe} :level) (eapi/get-logs *driver*)))

(defn read-toast []
  (eapi/wait-visible *driver*
                     {:data-ci "toast-message"}
                     {:message "Unable to read toast message"})
  (eapi/get-element-text *driver* {:data-ci "toast-message"}))

(defn get-toast []
  (eapi/with-http-error
    {:status (eapi/get-element-attr *driver* {:data-ci "toast-container"} "status")
     :message (eapi/get-element-text *driver* {:data-ci "toast-message"})}))

(defn close-toast []
  (eapi/click-visible *driver* {:data-ci "close-toast"}))

(defn driver-live? []
  (boolean (:type *driver*)))

(defn handle-ex-infos
  [e]
  (let [data (ex-data e)
        ex-info-message (or (:message data) (get-in data [:response :value :message]))
        message
        (cond
          ;; Driver crashed
          (not (driver-live?)) "Driver crashed."

          ;; Toast notification is covering the element to click
          (re-find #"Other element would receive the click: .*toast-container"
                   (get-in data [:response :value :message] ""))
          (let [{:keys [message]} (get-toast)]
            (cond-> "Toast notification is covering the element to click."
              (seq message) (str " Notification message: '" message "'")))

          ex-info-message
          (str "Unexpected ex-info thrown, message: \n" ex-info-message)

          :else "Unexpected ex-info thrown.")]
    (t/do-report {:actual e
                  :type :error
                  :message message})))

(defn handle-test-finally
  []
  (if (driver-live?)
    (do
      (is (empty? (get-severe-logs))
          (str "Severe logs found when testing: " (testing-contexts-str)))
      (when-let [{:keys [message status]} (get-toast)]
        (is (not= "error" status)
            (str "Error toast notification found after test. Notification message: " message))))
    (is false "Driver crashed.")))

(defmacro testing
  "Same as `clojure.test/testing` with the following additions:
  - wraps the body in a `try` block.
  - catches `ex-info`s, fails with more readable messages
  - adds an assertion in `finally` for checking whether there were some
  severe console logs during testing.
  - adds an assertion in `finally` for checking whether there are a error
  toast notification on the screen.

  The macro works only with the `with-driver` fixture"
  [string & body]
  `(t/testing ~string
     (try
       ~@body

       (catch clojure.lang.ExceptionInfo e#
         (handle-ex-infos e#))

       (finally
         (handle-test-finally)))))
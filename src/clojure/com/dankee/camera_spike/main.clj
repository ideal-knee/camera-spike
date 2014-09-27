(ns com.dankee.camera_spike.main
  (:use [neko.activity :only [defactivity set-content-view!]]
        [neko.threading :only [on-ui]]
        [neko.ui :only [make-ui]])
  (:require [com.dankee.camera_spike.saving-media :refer [get-output-image-uri]])
  (:import [android.provider MediaStore]) )

(def capture-image-activity-request-code 100)

(defactivity com.dankee.camera_spike.MainActivity

  :on-create
  (fn [activity bundle]
    (let [intent (android.content.Intent. MediaStore/ACTION_IMAGE_CAPTURE)]
      (doto intent
        (.putExtra MediaStore/EXTRA_OUTPUT (get-output-image-uri)) )
      (.startActivityForResult activity intent capture-image-activity-request-code)) )

  :on-activity-result
  (fn [activity request-code result-code intent]
    (on-ui
     (set-content-view! activity
       (make-ui [:linear-layout {}
                 [:text-view {:text "Hello from Clojure!"}] ] ) ) ) ) )


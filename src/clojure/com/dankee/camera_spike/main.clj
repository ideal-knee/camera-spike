(ns com.dankee.camera_spike.main
  (:use [neko.activity :only [defactivity set-content-view!]]
        [neko.threading :only [on-ui]]
        [neko.ui :only [make-ui]])
  (:require [com.dankee.camera_spike.saving-media :refer [get-output-image-uri]])
  (:import [android.app Activity]
           [android.graphics BitmapFactory]
           [android.provider MediaStore]
           [android.util Log]
           [android.widget Toast]) )

(def capture-image-activity-request-code 100)

(defn debug-tap [label object]
  (Log/d "Camera Spike" (str label ": " object))
  object )

(defn show-toast [activity text]
  (.show (Toast/makeText activity text Toast/LENGTH_LONG)) )

(let [file-uri (atom nil)]
  (defactivity com.dankee.camera_spike.MainActivity

    :on-create
    (fn [activity bundle]
      (swap! file-uri (fn [_] (get-output-image-uri)))
      (let [intent (android.content.Intent. MediaStore/ACTION_IMAGE_CAPTURE)]
        (doto intent
          (.putExtra MediaStore/EXTRA_OUTPUT @file-uri) )
        (.startActivityForResult activity intent capture-image-activity-request-code)) )

    :on-activity-result
    (fn [activity request-code result-code _]
      (on-ui
       (set-content-view! activity
                          (make-ui [:linear-layout {}
                                    [:text-view {:text "Hello from Clojure!"}] ] ) ) )

      (if (= request-code capture-image-activity-request-code)
        (condp = result-code

          Activity/RESULT_OK
          (let [bitmap (BitmapFactory/decodeFile (.getPath @file-uri))
                width (.getWidth bitmap)
                height (.getHeight bitmap) ]
            (show-toast activity (str width " x " height " image saved.")) )

          Activity/RESULT_CANCELED (show-toast activity "Cancelled :-(")
          (show-toast activity "Failure! >:-(") ) ) ) ) )


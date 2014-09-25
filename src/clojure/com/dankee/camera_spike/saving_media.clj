(ns com.dankee.camera_spike.saving-media
  (:import [android.net Uri]
           [android.os Environment]
           [android.util Log]
           [java.io File]
           [java.text SimpleDateFormat]
           [java.util Date] ) )

; Port of https://developer.android.com/guide/topics/media/camera.html#saving-media

(def media-type-image 1)
(def media-type-video 2)

(defn- create-media-file [type media-storage-dir]
  (let [timestamp (.format (new SimpleDateFormat "yyyyMMdd_HHmmss") (new Date))]
    (case type
      media-type-image (new File (str (.getPath media-storage-dir)
                                              File/separator
                                              "IMG_" timestamp ".jpg"))
      media-type-video (new File (str (.getPath media-storage-dir)
                                              File/separator
                                              "VID_" timestamp ".mp4"))
      nil ) ) )

(defn- get-output-media-file [type]
  (let [media-storage-dir (new File
                               (Environment/getExternalStoragePublicDirectory Environment/DIRECTORY_PICTURES)
                               "Camera Spike" )]
    (if (and (not (.exists media-storage-dir))
             (not (.mkdirs media-storage-dir)) )
      (Log/d "Camera Spike" "failed to create directory")
      (create-media-file type media-storage-dir) ) ) )

(defn- get-output-media-file-uri [type]
  (Uri/fromFile (get-output-media-file type)) )

(defn get-output-image-uri []
  (get-output-media-file-uri media-type-image) )

(defn get-output-video-uri []
  (get-output-media-file-uri media-type-video) )


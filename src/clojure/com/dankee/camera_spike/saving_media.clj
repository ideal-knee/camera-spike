(ns com.dankee.camera_spike.saving-media)

; Port of https://developer.android.com/guide/topics/media/camera.html#saving-media

(def media-type-image 1)
(def media-type-video 2)

(defn create-media-file [type media-storage-dir]
  (let [timestamp (.. (new java.text.SimpleDateFormat "yyyyMMdd_HHmmss")
                      (format (new java.util.Date)) )]
    (case type
      media-type-image (new java.io.File (str (.getPath media-storage-dir)
                                              java.io.File/separator
                                              "IMG_" timestamp ".jpg"))
      media-type-video (new java.io.File (str (.getPath media-storage-dir)
                                              java.io.File/separator
                                              "VID_" timestamp ".mp4"))
      nil ) ) )

(defn get-output-media-file [type]
  (let [media-storage-dir (new java.io.File
                               (android.os.Environment/getExternalStoragePublicDirectory
                                android.os.Environment/DIRECTORY_PICTURES )
                               "Camera Spike" )]
    (if (and (not (.exists media-storage-dir))
             (not (.mkdirs media-storage-dir)) )
      (android.util.Log/d "Camera Spike" "failed to create directory")
      (create-media-file type media-storage-dir) ) ) )

(defn get-output-media-file-uri [type]
  (android.net.Uri/fromFile (get-output-media-file type)) )


package com.zeelawahab.i160021_i160099;

public class ChatMessage {

        private String id;
        private String text;
        private String name;
        private String photoUrl;
        private String imageUrl;
        private String dateTime;
        public ChatMessage() {
        }

        public ChatMessage(String text, String name, String photoUrl, String imageUrl,String DT) {
            this.text = text;
            this.name = name;
            this.photoUrl = photoUrl;
            this.imageUrl = imageUrl;
            this.dateTime=DT;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public String getText() {
            return text;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    public String getDate() {
        return dateTime;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }
    }

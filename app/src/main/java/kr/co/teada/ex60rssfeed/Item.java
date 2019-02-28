package kr.co.teada.ex60rssfeed;

public class Item {

    String title;
    String link;
    String desc;
    String image;
    String date;

    //파싱할 때 갱신용 빈 생성자
    public Item(){

    }

    //2. 생성자 alt insult
    public Item(String title, String link, String desc, String image, String date) {
        this.title = title;
        this.link = link;
        this.desc = desc;
        this.image = image;
        this.date = date;
    }

    //3. getter setter alt insert 에서 getter and setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

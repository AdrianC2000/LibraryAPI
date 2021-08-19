package models;

public class BookRequirements {

    private String[] ID_book;
    private String[] title;
    private String[] author;
    private String[] is_taken;
    private String[] taken_by;
    private String[] taken_date;
    private String[] return_date;

    public String[] getID_book() {
        return ID_book;
    }

    public void setID_book(Integer[] ID_book) {
        String[] stringArrayID = new String[ID_book.length];
        for (int i = 0; i < stringArrayID.length; i++)
            stringArrayID[i] = (ID_book[i]).toString();
        this.ID_book = stringArrayID;
    }

    public String[] getTitle() {
        return title;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

    public String[] getAuthor() {
        return author;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    public String[] getIs_taken() {
        return is_taken;
    }

    public void setIs_taken(String[] is_taken) {
        this.is_taken = is_taken;
    }

    public String[] getTaken_by() {
        return taken_by;
    }

    public void setTaken_by(String[] taken_by) {
        this.taken_by = taken_by;
    }

    public String[] getTaken_date() {
        return taken_date;
    }

    public void setTaken_date(String[] taken_date) {
        this.taken_date = taken_date;
    }

    public String[] getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String[] return_date) {
        this.return_date = return_date;
    }
}

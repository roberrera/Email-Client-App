package roberterrera.com.email_client_app.Classes;


import java.io.Serializable;

/**
 * Created by Rob on 2/28/16.
 */
public class Email implements Serializable {

    private static final long serialVersionUID = 7526472295622776147L;

    public static Email instance;
    private String mSubjectLine;
    private String mFromUser;

    public Email(String fromUser, String subjectLine){
        mFromUser = "From: " + fromUser;
        mSubjectLine = "Subject: " + subjectLine;
    }

    public static Email getInstance() {
        return instance;
    }

    public String getmSubjectLine() {
        return mSubjectLine;
    }

    public void setmSubjectLine(String mSubjectLine) {
        this.mSubjectLine = mSubjectLine;
    }

    public String getmFromUser() {
        return mFromUser;
    }

    public void setmFromUser(String mFromUser) {
        this.mFromUser = mFromUser;
    }

}

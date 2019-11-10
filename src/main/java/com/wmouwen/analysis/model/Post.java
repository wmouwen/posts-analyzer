package com.wmouwen.analysis.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "row")
public class Post {

//    @XmlAttribute(name= "Id")
//    public Integer id;

//    @XmlAttribute(name= "PostTypeId")
//    public Integer postTypeId;

    @XmlAttribute(name = "AcceptedAnswerId")
    public Integer acceptedAnswerId;

    @XmlAttribute(name = "CreationDate")
    public Date creationDate;

    @XmlAttribute(name = "Score")
    public Integer score;

//    @XmlAttribute(name= "ViewCount")
//    public Integer viewCount;

    // HTML-encoded message body
//    @XmlAttribute(name= "Body")
//    public String body;

//    @XmlAttribute(name= "OwnerUserId")
//    public Integer ownerUserId;

//    @XmlAttribute(name= "LastEditorUserId")
//    public Integer lastEditorUserId;

//    @XmlAttribute(name= "LastEditDate")
//    public Date lastEditDate;

//    @XmlAttribute(name= "LastActivityDate")
//    public Date lastActivityDate;

    // Plain text title
//    @XmlAttribute(name= "Title")
//    public String title;

    // HTML-encoded list of tags
//    @XmlAttribute(name= "Tags")
//    public String tags;

//    @XmlAttribute(name= "AnswerCount")
//    public Integer answerCount;

//    @XmlAttribute(name= "CommentCount")
//    public Integer commentCount;

}
package com.miaxis.judicialcorrection.base.db.po;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * JusticeBureau
 *
 * @author zhangyw
 * Created on 5/4/21.
 */
@Entity(tableName = "JusticeBureau")
public class JusticeBureau {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private String teamId;
    private String teamName;
    private String teamAbbrName;
    private String teamGroup;
    private String teamLevel;
    private int teamOrder;
    private String teamTitle;
    private String isDeleted;
    private String description;
    private int topTeam;
    private String teamType;
    private String teamLiaison;
    private String liaisonMobile;
    private String teamPhone;
    private String teamAddress;
    private String teamFax;
    private String creatTime;
    private String updateTime;
    private String deleteTime;
    private String createId;
    private String createName;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private String ext6;

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamAbbrName(String teamAbbrName) {
        this.teamAbbrName = teamAbbrName;
    }

    public String getTeamAbbrName() {
        return teamAbbrName;
    }

    public void setTeamGroup(String teamGroup) {
        this.teamGroup = teamGroup;
    }

    public String getTeamGroup() {
        return teamGroup;
    }

    public void setTeamLevel(String teamLevel) {
        this.teamLevel = teamLevel;
    }

    public String getTeamLevel() {
        return teamLevel;
    }

    public void setTeamOrder(int teamOrder) {
        this.teamOrder = teamOrder;
    }

    public int getTeamOrder() {
        return teamOrder;
    }

    public void setTeamTitle(String teamTitle) {
        this.teamTitle = teamTitle;
    }

    public String getTeamTitle() {
        return teamTitle;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTopTeam(int topTeam) {
        this.topTeam = topTeam;
    }

    public int getTopTeam() {
        return topTeam;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType;
    }

    public String getTeamType() {
        return teamType;
    }

    public void setTeamLiaison(String teamLiaison) {
        this.teamLiaison = teamLiaison;
    }

    public String getTeamLiaison() {
        return teamLiaison;
    }

    public void setLiaisonMobile(String liaisonMobile) {
        this.liaisonMobile = liaisonMobile;
    }

    public String getLiaisonMobile() {
        return liaisonMobile;
    }

    public void setTeamPhone(String teamPhone) {
        this.teamPhone = teamPhone;
    }

    public String getTeamPhone() {
        return teamPhone;
    }

    public void setTeamAddress(String teamAddress) {
        this.teamAddress = teamAddress;
    }

    public String getTeamAddress() {
        return teamAddress;
    }

    public void setTeamFax(String teamFax) {
        this.teamFax = teamFax;
    }

    public String getTeamFax() {
        return teamFax;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getDeleteTime() {
        return deleteTime;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    public String getExt4() {
        return ext4;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }

    public String getExt5() {
        return ext5;
    }

    public void setExt6(String ext6) {
        this.ext6 = ext6;
    }

    public String getExt6() {
        return ext6;
    }

    @Override
    public String toString() {
        return "JusticeBureau{" +
                "id=" + id +
                ", teamId='" + teamId + '\'' +
                ", teamName='" + teamName + '\'' +
                ", teamAbbrName='" + teamAbbrName + '\'' +
                ", teamGroup='" + teamGroup + '\'' +
                ", teamLevel='" + teamLevel + '\'' +
                ", teamOrder=" + teamOrder +
                ", teamTitle='" + teamTitle + '\'' +
                ", isDeleted='" + isDeleted + '\'' +
                ", description='" + description + '\'' +
                ", topTeam=" + topTeam +
                ", teamType='" + teamType + '\'' +
                ", teamLiaison='" + teamLiaison + '\'' +
                ", liaisonMobile='" + liaisonMobile + '\'' +
                ", teamPhone='" + teamPhone + '\'' +
                ", teamAddress='" + teamAddress + '\'' +
                ", teamFax='" + teamFax + '\'' +
                ", creatTime='" + creatTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", deleteTime='" + deleteTime + '\'' +
                ", createId='" + createId + '\'' +
                ", createName='" + createName + '\'' +
                ", ext1='" + ext1 + '\'' +
                ", ext2='" + ext2 + '\'' +
                ", ext3='" + ext3 + '\'' +
                ", ext4='" + ext4 + '\'' +
                ", ext5='" + ext5 + '\'' +
                ", ext6='" + ext6 + '\'' +
                '}';
    }
}

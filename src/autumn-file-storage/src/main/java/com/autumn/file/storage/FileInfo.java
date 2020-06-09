package com.autumn.file.storage;

import com.autumn.util.ByteUtils;
import com.autumn.exception.ExceptionUtils;
import com.autumn.util.StringUtils;

/**
 * 文件信息
 *
 * @author 老码农 2019-03-13 09:42:51
 */
public class FileInfo {

    private final String name;
    private final String extensionName;
    private final String path;
    private final String fullPath;
    private final boolean file;
    private long length;

    /**
     * 实例化
     *
     * @param fullPath 完整名称
     * @param file     是否文件
     */
    public FileInfo(String fullPath, boolean file) {
        this(fullPath, file, 0L);
    }

    /**
     * 实例化
     *
     * @param fullPath 完整名称
     * @param file     是否文件
     * @param length   大小
     */
    public FileInfo(String fullPath, boolean file, long length) {
        if (StringUtils.isNullOrBlank(fullPath)) {
            ExceptionUtils.throwValidationException("完整路径不能为空。");
        }
        this.fullPath = StringUtils.removeStart(fullPath.trim().replace("\\", "/"), '/');
        if (file) {
            int fileIndex = this.fullPath.lastIndexOf("/");
            int spotIndex = this.fullPath.lastIndexOf(".");
            if (spotIndex >= 0 && spotIndex > fileIndex) {
                this.extensionName = this.fullPath.substring(spotIndex + 1);
            } else {
                this.extensionName = "";
            }
            if (fileIndex >= 0) {
                this.name = this.fullPath.substring(fileIndex + 1);
                this.path = this.fullPath.substring(0, this.fullPath.length() - this.name.length() - 1);
            } else {
                this.path = "";
                this.name = this.fullPath;
            }
            this.length = length;
        } else {
            this.extensionName = "";
            int fileIndex = this.fullPath.lastIndexOf("/");
            if (fileIndex >= 0) {
                this.name = this.fullPath.substring(fileIndex + 1);
            } else {
                this.name = "";
            }
            this.path = this.fullPath.substring(0, this.fullPath.length() - this.name.length() - 1);
            this.length = 0L;
        }
        this.file = file;
    }

    /**
     * 获取名称(文件名称或文件夹名称)
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * 获取扩展名
     *
     * @return
     */
    public String getExtensionName() {
        return this.extensionName;
    }

    /**
     * 获取路径
     *
     * @return
     */
    public String getPath() {
        return this.path;
    }

    /**
     * 获取完整路径(文件唯一id)
     *
     * @return
     */
    public String getFullPath() {
        return this.fullPath;
    }

    /**
     * 是否是文件
     *
     * @return
     */
    public boolean isFile() {
        return file;
    }

    /**
     * 获取大小
     *
     * @return 字节数
     */
    public long getLength() {
        return this.length;
    }

    /**
     * 设置文件大小
     *
     * @param length 大小
     */
    public void setLength(long length) {
        this.length = length;
    }

    /**
     * 获取长度字符
     *
     * @return
     */
    public String getLengthString() {
        return ByteUtils.getFileSize(this.getLength());
    }

    @Override
    public String toString() {
        return "name = " + this.getName() + " path = " + this.getPath() + " fullPath = " + this.getFullPath()
                + " file = " + this.isFile() + " length = " + this.getLength();
    }

}

package com.xzy.study.testcontentprovider

import android.content.ContentValues

import android.content.ContentUris

import android.content.UriMatcher
import android.content.ContentProvider
import android.database.AbstractWindowedCursor
import android.database.Cursor
import android.database.CursorWindow
import android.net.Uri
import android.os.Bundle
import androidx.annotation.Nullable


class Provider : ContentProvider() {
    private var uriMatcher: UriMatcher? = null
    private lateinit var userInfo: UserInfo

    data class UserInfo(var username: String?, var password: String?)

    override fun onCreate(): Boolean {
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher?.addURI(AUTHORITY, "/#", CMD_CONTROL_LANGUAGE)
        uriMatcher?.addURI(AUTHORITY, "/#", CMD_CONTROL_TIMEZONE)

        return false
    }

    class MyCursor(window: CursorWindow) : AbstractWindowedCursor() {
        private var mExtras: Bundle? = null

        init {
            setWindow(window)
        }

        fun setBundle(extras: Bundle?) {
            this.mExtras = extras
        }

        override fun getExtras(): Bundle {
            return Bundle(mExtras)
        }

        // 定义Client拿到的Cursor的行数
        override fun getCount(): Int {
            return window.numRows
        }

        // 定义Cursor的每一列的列名
        override fun getColumnNames(): Array<String> {
            return arrayOf("key", "value")
        }
    }

    @Nullable
    override fun query(uri: Uri, @Nullable projection: Array<String?>?, @Nullable selection: String?, @Nullable selectionArgs: Array<String?>?, @Nullable sortOrder: String?): Cursor? {
        val cursor = MyCursor(CursorWindow("test"))
        val bundle = Bundle()
        bundle.putString("username", userInfo.username)
        bundle.putString("password", userInfo.password)
        cursor.setBundle(bundle)
        return cursor
    }

    @Nullable
    override fun getType(uri: Uri): String? {
        return null
    }

    @Nullable
    override fun insert(uri: Uri, @Nullable values: ContentValues?): Uri {
        userInfo = UserInfo(values?.getAsString("username"), values?.getAsString("password"))
        val insertedUserUri: Uri = ContentUris.withAppendedId(CONTENT_URI, 1)
        // 发出通知给监听器(客户端)，告知数据已经改变
        context!!.contentResolver.notifyChange(insertedUserUri, null)
        return insertedUserUri
    }

    override fun delete(uri: Uri, @Nullable selection: String?, @Nullable selectionArgs: Array<String?>?): Int {
        return 0
    }

    override fun update(uri: Uri, @Nullable values: ContentValues?, @Nullable selection: String?, @Nullable selectionArgs: Array<String?>?): Int {
        return 0
    }

    companion object {
        // 文件所在包名
        private const val AUTHORITY = "com.xzy.study.testcontentprovider"

        // 指令类型
        const val CMD_CONTROL_LANGUAGE = 101
        const val CMD_CONTROL_TIMEZONE = 102

        // 对外的URI
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")
    }
}
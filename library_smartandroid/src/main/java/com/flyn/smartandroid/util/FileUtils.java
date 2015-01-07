package com.flyn.smartandroid.util;


import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Checksum;

/**
 * Created by flyn on 2014-12-24.
 */
public abstract class FileUtils
{
    public static void readFromFile(File targetFile, OutputStream output, int cacheBytesLength) throws IOException
    {
        if ((targetFile == null) || (output == null))
        {
            throw new NullPointerException();
        }
        if (cacheBytesLength <= 0)
        {
            throw new IllegalArgumentException("The parameter of cacheBytesLength should be great than zero.");
        }
        InputStream input = null;
        try
        {
            input = new FileInputStream(targetFile);
            readAndWrite(input, output, cacheBytesLength);
        } finally
        {
            if (input != null)
            {
                input.close();
            }
        }
    }

    public static void writeToFile(InputStream input, File targetFile, int cacheBytesLength) throws IOException
    {
        if ((input == null) || (targetFile == null))
        {
            throw new NullPointerException();
        }
        if (cacheBytesLength <= 0)
        {
            throw new IllegalArgumentException("The parameter of cacheBytesLength should be great than zero.");
        }
        OutputStream output = null;
        try
        {
            File parentFile = targetFile.getParentFile();
            if ((!parentFile.exists()) && (!parentFile.mkdirs()))
            {
                throw new IOException("could not create the path:" + parentFile.getPath());
            }
            output = new FileOutputStream(targetFile);
            readAndWrite(input, output, cacheBytesLength);
        } finally
        {
            if (output != null)
            {
                output.close();
            }
        }
    }

    public static void readAndWrite(InputStream input, OutputStream output, int cacheBytesLength) throws IOException
    {
        if ((input == null) || (output == null))
        {
            throw new NullPointerException();
        }
        if (cacheBytesLength <= 0)
        {
            throw new IllegalArgumentException("The parameter of cacheBytesLength should be great than zero.");
        }
        BufferedInputStream buffInput = new BufferedInputStream(input);
        BufferedOutputStream buffOutput = new BufferedOutputStream(output);
        byte[] b = new byte[cacheBytesLength];
        int len;
        while ((len = buffInput.read(b)) > 0)
        {
            buffOutput.write(b, 0, len);
        }
        buffOutput.flush();
    }

    public static void delDirectory(File f) throws IOException
    {
        if (f.isDirectory())
        {
            if (f.listFiles().length == 0)
            {
                if (!f.delete())
                {
                    throw new IOException("delete failure!");
                }
            } else
            {
                File[] delFile = f.listFiles();
                int i = delFile.length;
                for (int j = 0; j < i; j++)
                {
                    delDirectory(delFile[j]);
                }
                if (!f.delete())
                {
                    throw new IOException("delete failure!");
                }
            }
        } else if (!f.delete())
        {
            throw new IOException("delete failure!");
        }
    }

    public static List<File> recursionFile(File base, FileFilter filter, boolean listAll)
    {
        List<File> list = new LinkedList<File>();
        if ((filter == null) || (filter.accept(base)))
        {
            list.add(base);
            if (!listAll)
            {
                return list;
            }
        }
        if ((base != null) && (base.isDirectory()))
        {
            File[] f = base.listFiles();
            for (int i = 0; i < f.length; i++)
            {
                List<File> subList = recursionFile(f[i], filter, listAll);
                list.addAll(subList);
                if ((!listAll) && (list.size() > 0))
                {
                    return list;
                }
            }
        }
        return list;
    }

    public static byte[] readBytes(File file) throws IOException
    {
        FileInputStream is = new FileInputStream(file);
        return IoUtils.readAllBytesAndClose(is);
    }

    public static void writeBytes(File file, byte[] content) throws IOException
    {
        OutputStream out = new FileOutputStream(file);
        try
        {
            out.write(content);
        } finally
        {
            IoUtils.safeClose(out);
        }
    }

    public static String readUtf8(File file) throws IOException
    {
        return readChars(file, "UTF-8");
    }

    public static String readChars(File file, String charset) throws IOException
    {
        Reader reader = new InputStreamReader(new FileInputStream(file), charset);
        return IoUtils.readAllCharsAndClose(reader);
    }

    public static void writeUtf8(File file, CharSequence text) throws IOException
    {
        writeChars(file, "UTF-8", text);
    }

    public static void writeChars(File file, String charset, CharSequence text) throws IOException
    {
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), charset);
        IoUtils.writeAllCharsAndClose(writer, text);
    }

    /**
     * Copies a file to another location.
     */
    public static void copyFile(File from, File to) throws IOException
    {
        InputStream in = new BufferedInputStream(new FileInputStream(from));
        try
        {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(to));
            try
            {
                IoUtils.copyAllBytes(in, out);
            } finally
            {
                IoUtils.safeClose(out);
            }
        } finally
        {
            IoUtils.safeClose(in);
        }
    }

    /**
     * Copies a file to another location.
     */
    public static void copyFile(String fromFilename, String toFilename) throws IOException
    {
        copyFile(new File(fromFilename), new File(toFilename));
    }

    /**
     * To read an object in a quick & dirty way. Prepare to handle failures when
     * object serialization changes!
     */
    public static Object readObject(File file) throws IOException, ClassNotFoundException
    {
        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(fileIn));
        try
        {
            return in.readObject();
        } finally
        {
            IoUtils.safeClose(in);
        }
    }

    /**
     * To store an object in a quick & dirty way.
     */
    public static void writeObject(File file, Object object) throws IOException
    {
        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(fileOut));
        try
        {
            out.writeObject(object);
            out.flush();
            // Force sync
            fileOut.getFD().sync();
        } finally
        {
            IoUtils.safeClose(out);
        }
    }

    /**
     * @return MD5 digest (32 characters).
     */
    public static String getMd5(File file) throws IOException
    {
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        try
        {
            return IoUtils.getMd5(in);
        } finally
        {
            IoUtils.safeClose(in);
        }
    }

    /**
     * @return SHA-1 digest (40 characters).
     */
    public static String getSha1(File file) throws IOException
    {
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        try
        {
            return IoUtils.getSha1(in);
        } finally
        {
            IoUtils.safeClose(in);
        }
    }

    public static void updateChecksum(File file, Checksum checksum) throws IOException
    {
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        try
        {
            IoUtils.updateChecksum(in, checksum);
        } finally
        {
            IoUtils.safeClose(in);
        }
    }

    /**
     * 取得文件夹大小
     *
     * @param dirFile
     * @return
     * @throws Exception
     */
    public static long getFileSize(File dirFile)
    {
        long size = 0;
        File fileList[] = dirFile.listFiles();
        for (File file : fileList)
        {
            if (file.isDirectory())
            {
                size += getFileSize(file);
            } else
            {
                size += file.length();
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileSize
     * @return
     */
    public static String formatFileSize(long fileSize)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize == 0)
        {
            fileSizeString = "0.0MB";
        } else if (fileSize < 1024)
        {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576)
        {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824)
        {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        } else
        {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static String getRealPathFromURI(final Uri uri, final Context context)
    {

        if (uri == null)
            return null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri))
        {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type))
                {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri))
            {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type))
                {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type))
                {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type))
                {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme()))
        {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs)
    {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try
        {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst())
            {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally
        {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri)
    {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri)
    {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri)
    {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}

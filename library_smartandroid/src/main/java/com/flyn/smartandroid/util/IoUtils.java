package com.flyn.smartandroid.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Checksum;

/**
 * Created by flyn on 2014-12-24.
 * Utils for dealing with IO (streams, readers, ...).
 */
public class IoUtils
{
    private static final int BUFFER_SIZE = 4096;

    public static byte[] readAllBytes(InputStream in) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copyAllBytes(in, out);
        return out.toByteArray();
    }

    public static byte[] readAllBytesAndClose(InputStream in) throws IOException
    {
        try
        {
            return readAllBytes(in);
        } finally
        {
            safeClose(in);
        }
    }

    public static String readAllChars(Reader reader) throws IOException
    {
        char[] buffer = new char[2048];
        StringBuilder builder = new StringBuilder();
        while (true)
        {
            int read = reader.read(buffer);
            if (read == -1)
            {
                break;
            }
            builder.append(buffer, 0, read);
        }
        return builder.toString();
    }

    public static String readAllCharsAndClose(Reader reader) throws IOException
    {
        try
        {
            return readAllChars(reader);
        } finally
        {
            safeClose(reader);
        }
    }

    public static void writeAllCharsAndClose(Writer writer, CharSequence text) throws IOException
    {
        try
        {
            writer.append(text);
        } finally
        {
            safeClose(writer);
        }
    }

    public static void updateChecksum(InputStream in, Checksum checksum) throws IOException
    {
        byte[] buffer = new byte[BUFFER_SIZE];
        while (true)
        {
            int read = in.read(buffer);
            if (read == -1)
            {
                break;
            }
            checksum.update(buffer, 0, read);
        }
    }

    /**
     * @return MD5 digest (32 characters).
     */
    public static String getMd5(InputStream in) throws IOException
    {
        byte[] digest = getDigest(in, "MD5");
        return StringUtils.bytesToHexString(digest);
    }

    /**
     * @return SHA-1 digest (40 characters).
     */
    public static String getSha1(InputStream in) throws IOException
    {
        byte[] digest = getDigest(in, "SHA-1");
        return StringUtils.bytesToHexString(digest);
    }

    public static byte[] getDigest(InputStream in, String digestAlgo) throws IOException
    {
        MessageDigest digester;
        try
        {
            digester = MessageDigest.getInstance(digestAlgo);
        } catch (NoSuchAlgorithmException nsae)
        {
            throw new RuntimeException(nsae);
        }

        byte[] buffer = new byte[BUFFER_SIZE];
        while (true)
        {
            int read = in.read(buffer);
            if (read == -1)
            {
                break;
            }
            digester.update(buffer, 0, read);
        }
        return digester.digest();
    }

    /**
     * Copies all available data from in to out without closing any stream.
     *
     * @return number of bytes copied
     */
    public static int copyAllBytes(InputStream in, OutputStream out) throws IOException
    {
        int byteCount = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        while (true)
        {
            int read = in.read(buffer);
            if (read == -1)
            {
                break;
            }
            out.write(buffer, 0, read);
            byteCount += read;
        }
        return byteCount;
    }


    public static void safeClose(Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            } catch (IOException e)
            {
                // Silent
            }
        }
    }

}

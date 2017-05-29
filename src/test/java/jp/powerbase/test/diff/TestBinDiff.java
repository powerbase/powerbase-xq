package jp.powerbase.test.diff;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import jp.powerbase.file.BinaryDiff;
import jp.powerbase.file.BinaryPatch;
import jp.powerbase.file.Diff;
import jp.powerbase.file.Patch;
import jp.powerbase.util.FileUtil;
import jp.powerbase.util.IOUtil;

;
public class TestBinDiff {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		File oldFile = new File("C:/Users/hirai/difftest/4.xlsx");
		File newFile = new File("C:/Users/hirai/difftest/5.xlsx");
		File patchFile = new File("C:/Users/hirai/difftest/5.patch");
		File patchedFile = new File("C:/Users/hirai/difftest/p5.xlsx");
		try {
			System.out.println("File: 4 " + FileUtil.getHash(oldFile));

			Diff diff = new BinaryDiff();
			Patch patch = new BinaryPatch();

			InputStream d = diff.exec(oldFile, newFile);
			IOUtil.copy(d, patchFile);
			System.out.println("Patch: 5 " + FileUtil.getHash(patchFile));
			byte[] b = IOUtil.fileToByteArray(patchFile);
			InputStream p = new ByteArrayInputStream(b);
//			patch.exec(oldFile, p, patchedFile);

		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

}

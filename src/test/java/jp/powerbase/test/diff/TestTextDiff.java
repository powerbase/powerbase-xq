package jp.powerbase.test.diff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import jp.powerbase.file.Diff;
import jp.powerbase.file.Patch;
import jp.powerbase.file.TextDiff;
import jp.powerbase.file.TextPatch;

;
public class TestTextDiff {
	public static void main(String[] args) {
		File oldFile = new File("C:/Users/hirai/old.php");
		File newFile = new File("C:/Users/hirai/new.php");
		File patchFile = new File("C:/Users/hirai/php.php");
		try {
			/*
			diff_match_patch differ = new diff_match_patch();
			String oldF = TextReader.read(oldFile);
			String newF = TextReader.read(newFile);
		    LinkedList<jp.powerbase.test.diff.diff_match_patch.Diff> diffs = differ.diff_main(oldF, newF, true);
			LinkedList<jp.powerbase.test.diff.diff_match_patch.Patch> p = differ.patch_make(diffs);

			String pp = differ.patch_toText(p);
			IOUtil.copy(pp.getBytes(), diffFile);

			List<jp.powerbase.test.diff.diff_match_patch.Patch> pl = differ.patch_fromText(TextReader.read(diffFile));

			Object[] o = differ.patch_apply((LinkedList<jp.powerbase.test.diff.diff_match_patch.Patch>) pl, oldF);
		    String resultStr = (String)o[0];

			System.out.println(resultStr);
			*/
			Diff diff = new TextDiff();
			Patch patch = new TextPatch();

			InputStream d = diff.exec(oldFile, newFile);
			patch.exec(oldFile, d, patchFile);

		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}

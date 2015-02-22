package backup;

/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.nio.charset.Charset;
import java.nio.file.*;

import static java.nio.file.StandardCopyOption.*;

import java.nio.file.attribute.*;

import static java.nio.file.FileVisitResult.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Sample code that copies files in a similar manner to the cp(1) program.
 */

public class Backup {
	static Scanner sc = new Scanner(System.in);
	static List<String> exclusionList = new ArrayList<String>();

	/**
	 * Returns {@code true} if okay to overwrite a file ("cp -i")
	 */
	static boolean okayToOverwrite(Path file) {
		System.out.println("overwrite %s (yes/no)? " + file.toString());

		String answer = sc.next();
		return (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes"));
	}

	/**
	 * Copy source file to target location. If {@code prompt} is true then
	 * prompt user to overwrite target if it exists. The {@code preserve}
	 * parameter determines if file attributes should be copied/preserved. The
	 * {@code excludeList} parameter contains list of directories/files to be
	 * excluded from copying. If null then there is no exclusion.
	 */
	static void copyFile(Path source, Path target, boolean prompt,
			boolean preserve, List<String> excludeList) {
		boolean exclude = false;
		if (excludeList != null)
			for (int i = 0; i < excludeList.size(); i++)
				if (source.toString().contains(excludeList.get(i)))
					exclude = true;

		if (!exclude) {
			CopyOption[] options = (preserve) ? new CopyOption[] {
					COPY_ATTRIBUTES, REPLACE_EXISTING }
					: new CopyOption[] { REPLACE_EXISTING };

			String absolutePath = target.toAbsolutePath().toString();
			String filePath = absolutePath.substring(0,
					absolutePath.lastIndexOf(File.separator));
			String fileName = target.getFileName().toString();
			LocalDate localDate = LocalDate.now();
			String existingFilePath = filePath + "\\" + "_HISTORY_"
					+ localDate.toString() + "\\" + fileName;
			Path backupPath = Paths.get(existingFilePath);

			// If the file is existing, create a backup directory and copy the
			// old file there
			if (Files.exists(target)) {
				Path dir = Paths.get(filePath + "\\" + "_HISTORY_"
						+ localDate.toString());
				try {
					Files.createDirectories(dir);
				} catch (IOException e) {
					System.err.format("Unable to create:  %s: %s%n", dir, e);
				}
				try {
					Files.copy(target, backupPath, options);
				} catch (IOException x) {
					System.err.format("Unable to copy: %s: %s%n", target, x);
				}
			}

			if (!prompt || Files.notExists(target) || okayToOverwrite(target)) {
				try {
					Files.copy(source, target, options);
				} catch (IOException x) {
					System.err.format("Unable to copy: %s: %s%n", source, x);
				}
			}
		}
		else
			System.out.println(source + " excluded");
	}

	/**
	 * A {@code FileVisitor} that copies a file-tree ("cp -r")
	 */
	static class TreeCopier implements FileVisitor<Path> {
		private final Path source;
		private final Path target;
		private final boolean prompt;
		private final boolean preserve;
	
		TreeCopier(Path source, Path target, boolean prompt, boolean preserve) {
			this.source = source;
			this.target = target;
			this.prompt = prompt;
			this.preserve = preserve;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir,
				BasicFileAttributes attrs) {
			// before visiting entries in a directory we copy the directory
			// (okay if directory already exists).
			CopyOption[] options = (preserve) ? new CopyOption[] { COPY_ATTRIBUTES }
					: new CopyOption[0];

			Path newdir = target.resolve(source.relativize(dir));
			try {
				Files.copy(dir, newdir, options);
			} catch (FileAlreadyExistsException x) {
				// ignore
			} catch (IOException x) {
				System.err.format("Unable to create: %s: %s%n", newdir, x);
				return SKIP_SUBTREE;
			}
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
			copyFile(file, target.resolve(source.relativize(file)), prompt,
					preserve, exclusionList);
			return CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
			// fix up modification time of directory when done
			if (exc == null && preserve) {
				Path newdir = target.resolve(source.relativize(dir));
				try {
					FileTime time = Files.getLastModifiedTime(dir);
					Files.setLastModifiedTime(newdir, time);
				} catch (IOException x) {
					System.err.format(
							"Unable to copy all attributes to: %s: %s%n",
							newdir, x);
				}
			}
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			if (exc instanceof FileSystemLoopException) {
				System.err.println("cycle detected: " + file);
			} else {
				System.err.format("Unable to copy: %s: %s%n", file, exc);
			}
			return CONTINUE;
		}
	}

	static void usage() {
		System.err.println("java Copy [-ip] source... target");
		System.err.println("java Copy -r [-ip] source-dir... target");
		System.exit(-1);
	}

	public static void main(String[] args) throws IOException {
		boolean recursive = false;
		boolean prompt = false;
		boolean preserve = false;

		// process options
		int argi = 0;
		while (argi < args.length) {
			String arg = args[argi];
			if (!arg.startsWith("-"))
				break;
			if (arg.length() < 2)
				usage();
			for (int i = 1; i < arg.length(); i++) {
				char c = arg.charAt(i);
				switch (c) {
				case 'r':
					recursive = true;
					break;
				case 'i':
					prompt = true;
					break;
				case 'p':
					preserve = true;
					break;
				default:
					usage();
				}
			}
			argi++;
		}

		// remaining arguments are the source files(s) and the target location
		int remaining = args.length - argi;
		if (remaining < 2)
			usage();
		Path[] source = new Path[remaining - 1];
		int i = 0;
		while (remaining > 1) {
			source[i++] = Paths.get(args[argi++]);
			remaining--;
		}
		Path target = Paths.get(args[argi]);

		// check if target is a directory
		boolean isDir = Files.isDirectory(target);
		
		//TODO Create an exclusion file and check if everything works. P.s. File is not read :/
		File file = new File("C:/exclusion.txt");
		Charset charset = Charset.forName("UTF-8");
		Path fileP;
		fileP = file.toPath();

		try (BufferedReader reader = Files.newBufferedReader(fileP, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null && (line = reader.readLine()) != "" ) {
				exclusionList.add(line);
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
		if (exclusionList.isEmpty())
			exclusionList = null;

		
		// copy each source file/directory to target
		for (i = 0; i < source.length; i++) {
			Path dest = (isDir) ? target.resolve(source[i].getFileName())
					: target;

			if (recursive) {
				// follow links when copying files
				EnumSet<FileVisitOption> opts = EnumSet
						.of(FileVisitOption.FOLLOW_LINKS);
				TreeCopier tc = new TreeCopier(source[i], dest, prompt,
						preserve);
				Files.walkFileTree(source[i], opts, Integer.MAX_VALUE, tc);
			} else {
				// not recursive so source must not be a directory
				if (Files.isDirectory(source[i])) {
					System.err.format("%s: is a directory%n", source[i]);
					continue;
				}
				copyFile(source[i], dest, prompt, preserve, exclusionList);
			}
		}
		sc.close();
	}
}
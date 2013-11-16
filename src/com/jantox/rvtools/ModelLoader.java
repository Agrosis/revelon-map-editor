package com.jantox.rvtools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class ModelLoader {

	@SuppressWarnings("resource")
	public static Model loadRTM(String filename) {
		InputStream fis = null;
		BufferedReader br = null;
		String line = "";

		ArrayList<String> defs = new ArrayList<String>();

		try {
			fis = new FileInputStream(new File(filename).getAbsolutePath());
			br = new BufferedReader(new InputStreamReader(fis,
					Charset.forName("UTF-8")));
			while ((line = br.readLine()) != null) {
				defs.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<Vector3f> vertexes = new ArrayList<Vector3f>();
		ArrayList<Face> faces = new ArrayList<Face>();
		ArrayList<Vector3f> uvs = new ArrayList<Vector3f>();

		for (int i = 0; i < defs.size(); i++) {
			String data = defs.get(i);
			String[] params = data.split(" ");
			if (data.startsWith("v ")) {
				float x = Float.valueOf(params[1]);
				float y = Float.valueOf(params[2]);
				float z = Float.valueOf(params[3]);
				vertexes.add(new Vector3f(x, y, z));
			} else if (data.startsWith("f ")) {
				int fs = 3;

				if (params[1].equals("t")) { // its a texture
					if (params.length == 11)
						fs = 4;
				}

				Vector3f[] facevtxes = new Vector3f[fs];
				Vector3f[] facetex = new Vector3f[fs];

				for (int j = 3; j < 3 + fs; j++) {
					facevtxes[j - 3] = vertexes.get(Integer.valueOf(params[j]))
							.copy();
				}
				for (int j = 3 + fs; j < 3 + 2 * fs; j++) {
					facetex[j - (3 + fs)] = uvs.get(Integer.valueOf(params[j]))
							.copy();
				}

				Face f = new Face(facevtxes);
				f.tex = Integer.valueOf(params[2]);
				f.setTextureCoords(facetex);

				faces.add(f);
			} else if (data.startsWith("uv ")) {
				uvs.add(new Vector3f(Float.valueOf(params[1]), Float
						.valueOf(params[2]), 0));
			} else if (data.startsWith("tex ")) {
				Texture.loadTexture("textures/" + params[1], Texture.NEAREST);
			}
		}

		int uid = glGenLists(1);
		glNewList(uid, GL_COMPILE);

		glDisable(GL_TEXTURE_2D);

		for (int i = 0; i < faces.size(); i++) {
			Face f = faces.get(i);
			if (f.ta != null && (f.ta.x != f.ta.y)) {
				glEnable(GL_TEXTURE_2D);
				glBindTexture(GL_TEXTURE_2D, f.tex-1);
				if (f.isQuad()) {
					glBegin(GL_QUADS);

					glTexCoord2f((float) f.ta.x, (float) f.ta.y);
					glVertex3f((float) f.a.x, (float) f.a.y, (float) f.a.z);
					glTexCoord2f((float) f.tb.x, (float) f.tb.y);
					glVertex3f((float) f.b.x, (float) f.b.y, (float) f.b.z);
					glTexCoord2f((float) f.tc.x, (float) f.tc.y);
					glVertex3f((float) f.c.x, (float) f.c.y, (float) f.c.z);
					glTexCoord2f((float) f.td.x, (float) f.td.y);
					glVertex3f((float) f.d.x, (float) f.d.y, (float) f.d.z);
				} else {
					glBegin(GL_TRIANGLES);

					glTexCoord2f((float) f.ta.x, (float) f.ta.y);
					glVertex3f((float) f.a.x, (float) f.a.y, (float) f.a.z);
					glTexCoord2f((float) f.tb.x, (float) f.tb.y);
					glVertex3f((float) f.b.x, (float) f.b.y, (float) f.b.z);
					glTexCoord2f((float) f.tc.x, (float) f.tc.y);
					glVertex3f((float) f.c.x, (float) f.c.y, (float) f.c.z);
				}
			} else {
				if (f.isQuad()) {
					glBegin(GL_QUADS);

					glVertex3f((float) f.a.x, (float) f.a.y, (float) f.a.z);
					glVertex3f((float) f.b.x, (float) f.b.y, (float) f.b.z);
					glVertex3f((float) f.c.x, (float) f.c.y, (float) f.c.z);
					glVertex3f((float) f.d.x, (float) f.d.y, (float) f.d.z);

				} else {
					glBegin(GL_TRIANGLES);

					glVertex3f((float) f.a.x, (float) f.a.y, (float) f.a.z);
					glVertex3f((float) f.b.x, (float) f.b.y, (float) f.b.z);
					glVertex3f((float) f.c.x, (float) f.c.y, (float) f.c.z);
				}
			}

			glEnd();
		}

		glEndList();

		return new Model(uid, faces);
	}

	public static Model loadDSM(String filename) throws IOException {
		File file = new File(filename);
		byte[] fileData = new byte[(int) file.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		dis.readFully(fileData);
		dis.close();

		ByteBuffer buffer = ByteBuffer.wrap(fileData);
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		ArrayList<Vector3f> vertexes = new ArrayList<Vector3f>();
		ArrayList<Vector3f> faces = new ArrayList<Vector3f>();

		short vsize = buffer.getShort();

		for (int i = 0; i < vsize; i++) {
			float x = buffer.getFloat();
			float y = buffer.getFloat();
			float z = buffer.getFloat();

			vertexes.add(new Vector3f(x, y, z));
		}

		short fsize = buffer.getShort();

		for (int i = 0; i < fsize; i++) {
			short a = buffer.getShort();
			short b = buffer.getShort();
			short c = buffer.getShort();

			faces.add(new Vector3f(a, b, c));
		}

		int uid = glGenLists(1);
		glNewList(uid, GL_COMPILE);

		glBegin(GL_TRIANGLES);

		for (int i = 0; i < faces.size(); i++) {
			Vector3f a = vertexes.get((int) faces.get(i).x);
			Vector3f b = vertexes.get((int) faces.get(i).y);
			Vector3f c = vertexes.get((int) faces.get(i).z);

			glVertex3f((float) a.x, (float) a.y, (float) a.z);
			glVertex3f((float) b.x, (float) b.y, (float) b.z);
			glVertex3f((float) c.x, (float) c.y, (float) c.z);
		}

		glEnd();

		glEndList();

		return new Model(uid);
	}

}

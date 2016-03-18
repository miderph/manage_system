package com.onewave.backstage.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.zhilink.tools.ApkTools;
import net.zhilink.tools.InitManager;
import net.zhilink.tools.ZipUtils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.onewave.backstage.service.AppDownloadUrlService;

@Controller("filesController")
@RequestMapping("/files/*")
public class FileUpload extends MultiActionController {
	private Logger logger = Logger.getLogger(FileUpload.class);
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat dateformat1 = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	@Autowired
	private AppDownloadUrlService appDownloadUrlService;

	@Transactional
	@RequestMapping("file_upload.do")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");

		String obj_id = request.getParameter("obj_id");// 参数拼在url后，所以能直接获取，否则要先解析多段
		String type = request.getParameter("type");
		String apkUrl = "";
		String fileUrl = "";
		String tempFileUrl = "";

		// 文件名中文乱码问题，可调用ServletUpLoader的setHeaderEncoding方法，或者设置request的setCharacterEncoding属性
		boolean isMultipart = ServletFileUpload.isMultipartContent(request); // 判断上传表单是否为multipart/form-data类型
		if (isMultipart) {
			// 创建磁盘工厂，利用构造器实现内存数据储存量和临时储存路径
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(4096);
			// 设置文件临时存储路径
			factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
			logger.info("upload tmpdir : "
					+ System.getProperty("java.io.tmpdir"));
			// 产生一新的文件上传处理程式
			ServletFileUpload upload = new ServletFileUpload(factory);
			ProgressListener progressListener = new UploadProgressListener(
					request);
			request.getSession().setAttribute("readPrcnt_" + obj_id, "0%");
			// request.getSession().setAttribute("progress", progressListener);
			upload.setProgressListener(progressListener);
			// 文件名中文乱码问题，可调用ServletUpLoader的setHeaderEncoding方法，或者设置request的setCharacterEncoding属性
			upload.setSizeMax(1024 * 1024 * 500); // 设置允许用户上传文件大小,单位:字节
			FileOutputStream out = null;
			InputStream in = null;
			// 得到所有的表单域，它们目前都被当作FileItem
			try {
				// 解析request对象，并把表单中的每一个输入项包装成一个fileItem
				// 对象，并返回一个保存了所有FileItem的list集合。
				// upload对象是使用DiskFileItemFactory
				// 对象创建的ServletFileUpload对象，并设置了临时文件路径 传输文件大小等等。
				List<FileItem> list = upload.parseRequest(request);
				Iterator it = list.iterator();
				while (it.hasNext()) {
					FileItem item = (FileItem) it.next();// 每一个item就代表一个表单输出项
					String filename = item.getName();
					if (item.getName() != null && !item.isFormField()) {
						// 得到上传文件的名称,并截取
						String extensionName = filename.substring(filename
								.lastIndexOf(".") + 1);// 获取文件后缀名
						if (type.equals("zip")) {
							apkUrl = "html/" + obj_id + "."
									+ extensionName.toLowerCase();
							fileUrl = InitManager.getRootLocalPath() + apkUrl;
							tempFileUrl = fileUrl;// zip上传不保存为临时文件
						} else {
							apkUrl = "upload/"
									+ (new SimpleDateFormat("yyyyMM")
											.format(new Date())) + "/" + obj_id
									+ "." + extensionName;
							fileUrl = InitManager.getRootLocalPath() + apkUrl;
							tempFileUrl = fileUrl + "_temp";
						}
						File f = new File(tempFileUrl);
						if (!f.exists()) {
							f.getParentFile().mkdirs();
						}
						if (f.exists()) {
							f.delete();
						}

						logger.info("tempFileUrl=" + tempFileUrl);
						out = new FileOutputStream(tempFileUrl);
						in = item.getInputStream();
						byte buffer[] = new byte[1024];
						int len = 0;
						while ((len = in.read(buffer)) > 0) {
							out.write(buffer, 0, len);
						}
						in.close();
						out.close();
						if (type.equals("zip")) {
							response.setContentType("application/json;charset=UTF-8");
							JSONObject json = new JSONObject();
							try {
								String destDir = InitManager.getRootLocalPath()
										+ "html/" + obj_id + "/";
								File destFileDir = new File(destDir);
								if (destFileDir.exists()) {
									File tempDestFileDir = new File(
											destDir.substring(0,
													destDir.length() - 1)
													+ "_old_"
													+ dateformat1
															.format(new java.util.Date())
													+ "/");
									logger.info("destDir 【" + destFileDir
											+ "】 exists,rename to 【"
											+ tempDestFileDir.getAbsolutePath()
											+ "】");
									destFileDir.renameTo(tempDestFileDir);
								}
								ZipUtils.unZip(tempFileUrl, destDir);
								json.put("success", true);
								json.put("info", "上传成功");
								String zip_url = "html/" + obj_id + "/"
										+ findIndexHtml(destDir);// "html/"
																	// +obj_id+"/about.html";
								String zip_url_show = InitManager
										.combineRootHttpPath(zip_url);
								String zip_download_url_show = InitManager
										.getRootHttpPath()
										+ "/html/"
										+ obj_id
										+ ".zip";

								json.put("zip_url", zip_url);
								json.put("zip_url_show", zip_url_show);
								json.put("zip_download_url_show",
										zip_download_url_show);
								logger.info(json.toString());
							} catch (Exception e) {
								json.put("success", false);
								json.put("info", "解压失败，请检查压缩包重新上传");
								json.put("zip_url", "");
								json.put("zip_url_show", "");
								json.put("zip_download_url_show", "");
								e.printStackTrace();
							}
							response.getWriter().print(json.toString());
						} else {
							response.setContentType("application/json;charset=UTF-8");
							JSONObject json = new JSONObject();
							Map<String, String> info = ApkTools
									.getApkInfo(tempFileUrl);
							String resultStr = null;
							if (info.get("package_name") == null) {
								resultStr = "上传失败！未获取到包名.";
							} else {
								resultStr = appDownloadUrlService
										.saveDownloadUrl(obj_id, "0", apkUrl,
												"", "", null, info);
							}
							if (resultStr != null) {
								json.put("success", false);
								json.put("info", resultStr);
							} else {
								logger.info("apkInfo " + info);
								json.put("success", true);
								json.put("info", "上传成功");
								json.put("apk_url", apkUrl);
								json.put("file_size", info.get("file_size"));
								json.put("version", info.get("version"));
								json.put("version_code",
										info.get("versionCode"));
								json.put("md5sum", info.get("md5sum"));
								json.put("package_name",
										info.get("package_name"));

								File oldFile = new File(fileUrl);
								File newFile = new File(tempFileUrl);
								oldFile.delete();
								newFile.renameTo(oldFile);
							}

							logger.info(json.toString());
							response.getWriter().print(json.toString());
						}
					}
					item.delete();
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
				response.getWriter().print(
						"{'success':false,'info':'上传失败，请重新上传，" + e.getMessage()
								+ "'}");
			} finally {
				try {
					request.getSession().setAttribute("readPrcnt_" + obj_id,
							"100%");
					// request.getSession().removeAttribute("");
					response.getWriter().close();
					in.close();
					out.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private String findIndexHtml(String rootPath) {
		String indexFileName = "";
		try {
			List<File> fileList = new ArrayList<File>();
			File root = new File(rootPath);
			fileList.add(root);
			loop_while: while (fileList.size() > 0) {
				File file = fileList.remove(0);
				if (file.isDirectory()) {
					File[] files = file.listFiles();
					for (File afile : files) {
						fileList.add(afile);
					}
					continue loop_while;
				} else if (!file.isFile()) {
					continue loop_while;
				}

				String name = file.getName();
				if ("index.html".equals(name) || "index.htm".equals(name)) {// index.html最优先
					indexFileName = file.getAbsolutePath();
					break loop_while;
				}
				if (StringUtils.isEmpty(indexFileName)
						&& (name.endsWith(".html") || name.endsWith(".htm"))) {// 必须为.html
					indexFileName = file.getAbsolutePath();
				} else if ("about.html".equals(name)
						|| "about.htm".equals(name)) {// about.html优先
					indexFileName = file.getAbsolutePath();
				} else {
					// 忽略其他文件名
				}
			}
			fileList.clear();
			if (StringUtils.isEmpty(indexFileName))
				indexFileName = "about.html";
			else {
				indexFileName = indexFileName.substring(rootPath.length())
						.replace("\\", "/");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return indexFileName;
	}

	@RequestMapping("file_upload_progress.do")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String obj_id = (String) req.getParameter("obj_id");
			String percent = (String) req.getSession().getAttribute(
					"readPrcnt_" + obj_id);
			if (StringUtils.isBlank(percent)) {
				percent = "0%";
			}
			logger.info("get percent " + req.getLocalAddr() + "||"
					+ req.getRemoteHost() + "||" + obj_id + "||" + percent);
			resp.getWriter().print(percent);
		} finally {
			try {
				resp.getWriter().close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public class UploadProgressListener implements ProgressListener {// 要继承ProgressListener
		private double megaBytes = -1;
		private HttpSession session;
		private HttpServletRequest request;

		public UploadProgressListener(HttpServletRequest request) {
			this.request = request;
			session = request.getSession();
		}

		public void update(long pBytesRead, long pContentLength, int pItems) {
			String obj_id = (String) request.getParameter("obj_id");
			logger.debug("pBytesRead : " + pBytesRead + " pContentLength "
					+ pContentLength + " pItems :" + pItems);
			double mBytes = pBytesRead / 100000;
			double total = pContentLength / 100000;
			if (megaBytes == mBytes) {
				logger.debug("megaBytes == mBytes ; megaBytes "
						+ String.valueOf(megaBytes) + " mBytes:"
						+ String.valueOf(mBytes));
				return;
			}
			logger.debug("total====>" + total);
			logger.debug("mBytes====>" + mBytes);
			megaBytes = mBytes;
			logger.debug("megaBytes====>" + megaBytes);
			logger.debug("We are currently reading item " + pItems);
			if (pContentLength == -1) {
				logger.debug("So far, " + pBytesRead + " bytes have been read.");
			} else {
				logger.debug("So far, " + pBytesRead + " of " + pContentLength
						+ " bytes have been read.");
				double read = ((pBytesRead * 1.0) / (pContentLength * 1.0));

				if (read == Double.NaN) {
					read = 0;
				}
				NumberFormat nf = NumberFormat.getPercentInstance();
				logger.info(obj_id + " upload readPrcnt===>" + nf.format(read));// 生成读取的百分比
																				// 并放入session中
				session.setAttribute("readPrcnt_" + obj_id, nf.format(read));
			}
		}
	}

}

package alves.renan.benchmarkapp.benchimage.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import alves.renan.benchmarkapp.benchimage.dao.ResultDao;
import alves.renan.benchmarkapp.benchimage.dao.model.ResultImage;
import alves.renan.benchmarkapp.benchimage.dao.model.AppConfiguration;
import alves.renan.benchmarkapp.benchimage.util.TaskResultAdapter;
import br.ufc.mdcc.util.ImageUtils;


public final class ImageFilterTask extends AsyncTask<Void, String, ResultImage> {
	private final String clsName = ImageFilterTask.class.getName();
	private PowerManager.WakeLock wakeLock;

	@SuppressLint("StaticFieldLeak")
	private Context context;
	private Filter filter;
	private AppConfiguration config;
	private TaskResultAdapter<ResultImage> taskResult;

	private ResultDao dao;
	private ResultImage result = null;

	public ImageFilterTask(Context context, Filter filter, AppConfiguration config, TaskResultAdapter<ResultImage> taskResult) {
		this.context = context;
		this.filter = filter;
		this.config = config;
		this.taskResult = taskResult;

		result = new ResultImage(config);
		dao = new ResultDao(context);
	}

	@Override
	protected void onPreExecute() {
		preventSleep();
	}

	@Override
	protected ResultImage doInBackground(Void... params) {
		try {
			switch (config.getFilter()) {
				case "Benchmark":
					Log.i(clsName, "Iniciou processo de Benchmark");
					benchmarkTask();
					break;
				case "Original":
					originalTask();
					break;
				case "Cartoonizer":
					cartoonizerTask();
					break;
				case "Sharpen":
					sharpenTask();
					break;
				default:
					filterMapTask();
					break;
			}
			return result;
		} catch (InterruptedException e) {
			Log.w(clsName, e);
		} catch (IOException e) {
			Log.w(clsName, e);
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		taskResult.taskOnGoing(0, values[0]);
	}

	@Override
	protected void onPostExecute(ResultImage result) {
		wakeLock.release();
		taskResult.completedTask(result);
	}

	private String sizeToPath(String size) {
		switch (size) {
			case "8MP":
				return "images/8mp/";
			case "4MP":
				return "images/4mp/";
			case "2MP":
				return "images/2mp/";
			case "1MP":
				return "images/1mp/";
			case "0.3MP":
				return "images/0_3mp/";
		}

		return null;
	}

	private String generatePhotoFileName() {
		StringBuilder sb = new StringBuilder();
		sb.append(config.getImage().replace(".jpg", "")).append("_").append(config.getFilter()).append("_");
		switch (config.getSize()) {
			case "0.7MP":
				sb.append("0_7mp.jpg");
				break;
			case "0.3MP":
				sb.append("0_3mp.jpg");
				break;
			default:
				sb.append(config.getSize()).append(".jpg");
				break;
		}
		return sb.toString();
	}

//	private File saveResultOnStorage(byte[] data) throws IOException {
//		File file = new File(config.getOutputDirectory(), generatePhotoFileName());
//		OutputStream output = new FileOutputStream(file);
//		output.write(data);
//		output.flush();
//		output.close();
//		return file;
//	}

	@SuppressLint("InvalidWakeLockTag")
	private void preventSleep() {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PicFilter CPU");
		wakeLock.acquire();
	}

	private void originalTask() throws IOException {
		long initialTime = System.currentTimeMillis();

		publishProgress("Carregando imagem");

		Bitmap image = decodeSampledBitmapFromResource(context, sizeToPath(config.getSize()) + config.getImage(), config.getSize());
		result.setTotalTime(System.currentTimeMillis() - initialTime);
		result.setBitmap(image);

		publishProgress("Imagem carregada!");
	}

	private void filterMapTask() throws IOException {
		long initialTime = System.currentTimeMillis();
		publishProgress("Aplicando " + config.getFilter());

		byte[] mapFilter = null;
		switch (config.getFilter()) {
			case "Red Ton":
				mapFilter = ImageUtils.streamToByteArray(context.getAssets().open("filters/map1.png"));
				break;
			case "Blue Ton":
				mapFilter = ImageUtils.streamToByteArray(context.getAssets().open("filters/map3.png"));
				break;
			case "Yellow Ton":
				mapFilter = ImageUtils.streamToByteArray(context.getAssets().open("filters/map2.png"));
				break;
		}

		byte[] image = ImageUtils.streamToByteArray(context.getAssets().open(sizeToPath(config.getSize()) + config.getImage()));
		byte[] imageResult = filter.mapTone(image, mapFilter);

//		File fileSaved = saveResultOnStorage(imageResult);
		result.setTotalTime(System.currentTimeMillis() - initialTime);
//		result.setBitmap(decodeSampledBitmapFromResource(context, new FileInputStream(fileSaved), config.getSize()));
		result.setBitmap( BitmapFactory.decodeByteArray(imageResult, 0, imageResult.length) );

		dao.add(result);

		publishProgress("Terminou Processamento!");

		imageResult = null;
		image = null;
		mapFilter = null;
		System.gc();
	}

	private void sharpenTask() throws IOException {
		long initialTotalTime = System.currentTimeMillis();

		publishProgress("Aplicando Sharpen");

		byte[] image = ImageUtils.streamToByteArray(context.getAssets().open(sizeToPath(config.getSize()) + config.getImage()));
		double[][] mask = {{-1, -1, -1}, {-1, 9, -1}, {-1, -1, -1}};
		double factor = 1.0;
		double bias = 0.0;

		byte[] imageResult = filter.filterApply(image, mask, factor, bias);

//		File fileSaved = saveResultOnStorage(imageResult);
		result.setTotalTime(System.currentTimeMillis() - initialTotalTime);
//		result.setBitmap(decodeSampledBitmapFromResource(context, new FileInputStream(fileSaved), config.getSize()));
		result.setBitmap( BitmapFactory.decodeByteArray(imageResult, 0, imageResult.length) );

		dao.add(result);

		publishProgress("Sharpen Completo!");

		imageResult = null;
		image = null;
		System.gc();
	}

	private void cartoonizerTask() throws IOException, InterruptedException {
		long initialTime = System.currentTimeMillis();

		publishProgress("Aplicando Cartoonizer");

		byte[] image = ImageUtils.streamToByteArray(context.getAssets().open(sizeToPath(config.getSize()) + config.getImage()));
		byte[] imageResult = filter.cartoonizerImage(image);

//		File fileSaved = saveResultOnStorage(imageResult);
		result.setTotalTime(System.currentTimeMillis() - initialTime);
//		result.setBitmap(decodeSampledBitmapFromResource(context, new FileInputStream(fileSaved), config.getSize()));
		result.setBitmap( BitmapFactory.decodeByteArray(imageResult, 0, imageResult.length) );

		dao.add(result);

		publishProgress("Cartoonizer Completo!");

		imageResult = null;
		image = null;
		System.gc();
	}

	private void benchmarkTask() throws IOException, InterruptedException {
		long totalTime = 0L;

		File fileSaved = null;

		int count = 1;
		String[] sizes = {"8MP", "4MP", "2MP", "1MP", "0.3MP"};
		for (String size : sizes) {
			byte[] image = ImageUtils.streamToByteArray(context.getAssets().open(sizeToPath(size) + config.getImage()));
			
			config.setSize(size);
			for (int i = 0; i < 3; i++) {
				publishProgress("Benchmark [" + (count++) + "/15]");

				long initialTime = System.currentTimeMillis();
				byte[] imageResult = filter.cartoonizerImage(image);
//				fileSaved = saveResultOnStorage(imageResult);

				ResultImage resultImage = new ResultImage(config);
				resultImage.setTotalTime(System.currentTimeMillis() - initialTime);
				
				dao.add(resultImage);
				totalTime += resultImage.getTotalTime();
				
				if (count != 16) {
					imageResult = null;
					System.gc();
					Thread.sleep(750);
				}
			}
			image = null;
			System.gc();
		}

		result.setTotalTime(totalTime);
		result.setBitmap(decodeSampledBitmapFromResource(context, new FileInputStream(fileSaved), "0.3MP"));
		result.getConfig().setSize("Todos");

		dao.add(result);
		publishProgress("Benchmark Completo!");
	}

	private Bitmap decodeSampledBitmapFromResource(Context context, InputStream is, String size) throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inMutable = true;

		switch (size) {
			case "1MP":
			case "2MP":
				options.inSampleSize = 2;
				break;
			case "4MP":
				options.inSampleSize = 4;
				break;
			case "6MP":
				options.inSampleSize = 6;
				break;
			case "8MP":
				options.inSampleSize = 8;
				break;
			default:
				options.inSampleSize = 1;
				break;
		}

		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(is, null, options);
	}

	private Bitmap decodeSampledBitmapFromResource(Context context, String path, String size) throws IOException {
		return decodeSampledBitmapFromResource(context, context.getAssets().open(path), size);
	}
}

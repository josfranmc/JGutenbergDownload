package org.josfranmc.gutenberg.download;

/**
 * Factoría que permite crear un objeto para realizar un tipo determinado de descarga 
 * @author Jose Francisco Mena Ceca
 * @version 1.0
 * @see AbstractDownload
 * @see DownloadMode
 */
public class DownloadFactory {

	/**
	 * Crea un objeto concreto para realizar descargas 
	 * @param type tipo de descarga según DownloadType
	 * @return objeto para realizar descargas
	 */
	public static AbstractDownload create(DownloadMode type)  {
		AbstractDownload downloadtype = null;
		
		switch (type) {
		case SOFT:
			downloadtype = new DownloadSoft();
			break;
		case GREEDY:
			downloadtype = new DownloadGreedy();
			break;
		default:
			downloadtype = new DownloadSoft();
			break;
		}
		return downloadtype;
	}
}
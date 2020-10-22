package alves.renan.benchmarkapp.benchimage.image;

import br.ufc.mdcc.mpos.offload.Remotable;

public interface InternetFilter extends Filter {
	@Remotable(status = true, value = Remotable.Offload.STATIC)
	byte[] mapTone(byte[] source, byte[] map);

	@Remotable(status = true, value = Remotable.Offload.STATIC)
	byte[] filterApply(byte[] source, double[][] filter, double factor, double offset);

	@Remotable(status = true, value = Remotable.Offload.STATIC)
	byte[] cartoonizerImage(byte[] source);
}
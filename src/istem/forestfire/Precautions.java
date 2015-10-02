package istem.forestfire;

import istem.forestfire.R;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

/**
 * Created by Hp on 8/18/2015.
 */
public class Precautions extends ActionBarActivity {

	private TextView precautions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.precautions);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon);
		getSupportActionBar().setTitle("Precautions");

		precautions = (TextView) findViewById(R.id.precautions);

		// ---------------------------------------------------------------------//
		// Write Precautions Here
		precautions.setText("here hsre ere hs s sh hfd hfd");
		StringBuilder str = new StringBuilder();
		str.append("1. Put out matches and cigarettes and do not throw them out car windows.\n\n");
		str.append("2. Do not leave bottles, glass or litter in the countryside that could cause or contribute to fire.\n\n");
		str.append("3. Do not light bonfires for any use (cooking, heating, etc. ). Note that, in general, the use of fire is prohibited in any forest area. Its use is authorised only in very specific areas and seasons (recreation areas equipped specifically for this purpose  and outside of times of drought or hazard). Even in approved areas, be especially careful to put fire out, making sure there are no remaining embers that could reignite and start a fire.\n\n");
		str.append("4. No not leave bonfires or lamps burning when you leave the camp.\n\n");
		str.append("5. If you see a forest fire or smoke plume in the forest, it is important to report it as quickly as possible to one of the nearest emergency services: 112 telephone number, Forest Services, Fire Fighters, Police, Civil Guard or Civil Protection.\n\n");
		str.append("6. Monitor the burn and do not leave until you are sure that it is completely out.\n\n");
		str.append("7. If you smoke outside, put out your cigarette butt on a rock or bury it in the ground.\n\n");
		str.append("8. Always have an emergency kit within reach (at the cottage, in your backpack when hiking in the forest.\n\n");
		str.append("9. Control vegetation around your home.\n\n");
		str.append("10. Store building materials, firewood and propane tanks more than 10 m away from any building on your land; clear away all vegetation within a radius of 3 m of the propane storage tank in order to reduce the risk of a fire spreading.\n\n");
		str.append("11. Keep near your home a hose or a water supply of at least 200 litres in order to intervene promptly if a fire starts.\n\n");
		str.append("12. Choose a cleared location, out of the wind, for a fire outside; have a shovel, a bucket of water or a rake nearby, constantly monitor your fire and, to extinguish it, spray it with abundant water and cover it with ash, sand or earth.\n\n");
		str.append("13. Burn anything (waste, dead leaves) at the end of the day, when there is no wind, far from vegetation and in compliance with municipal by-laws.\n\n");
		str.append("14. If you live in a country house or in a residential development, chimneys of houses must have spark arrestors.\n\n");

		precautions.setText(str.toString());

	}
}

package com.tapster.barMenu;

import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.val;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.tapster.R;
import com.tapster.azureConnectivity.AzureServiceConnection;
import com.tapster.azureConnectivity.Config;
import com.tapster.customer.CollectOrderActivity;
import com.tapster.data.OrderList;
import com.tapster.ui.OrderListAdapter;

public class PendingOrderFragment extends Fragment
{

	private  ArrayList<OrderList> items = new ArrayList<OrderList>();
	private  OrderListAdapter adapter;

	private Context ctx;
	private View rootView;
	private RelativeLayout overlay;

	@Override
	public void onResume()
	{
		super.onResume();
		refreshOrders();
	}

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{

		ctx = getActivity().getApplicationContext();
		rootView = inflater.inflate(R.layout.fragment_pending_order, container, false);

		// Set overlay progress bar.
		overlay = (RelativeLayout) rootView.findViewById(R.id.overlay);

		ListView POText = (ListView) rootView.findViewById(R.id.order_list);
		adapter = new OrderListAdapter(ctx, R.layout.listentry_pendingorder, items);
		adapter.setNotifyOnChange(true);

		POText.setAdapter(adapter);

		ClickPendingOrder();
		return rootView;
	}

	private void refreshOrders()
	{
		overlay.setVisibility(RelativeLayout.VISIBLE);

		MobileServiceClient mClient = AzureServiceConnection.instance.getClient();
		MobileServiceTable<OrderList> mpendingOrderTable = mClient.getTable(Config.PendingOrderTable, OrderList.class);

		String userId = mClient.getCurrentUser().getUserId();

		mpendingOrderTable.where().field("userid").eq(val(userId)).execute(new TableQueryCallback<OrderList>()
		{

			public void onCompleted(List<OrderList> result, int count,
					Exception exception, ServiceFilterResponse response)
			{
				overlay.setVisibility(RelativeLayout.INVISIBLE);
				if (exception == null)
				{
					adapter.clear();

					for (OrderList item : result)
					{
						item.deSerialize();
						if (item != null)
							adapter.add(item);
					}
				} else
					Toast.makeText(getActivity().getApplicationContext(), "Error in fetching orders", Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void ClickPendingOrder()
	{
		ListView CategoryList = (ListView) rootView.findViewById(R.id.order_list);
		CategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id)
			{
				OrderList temp = items.get(position);
				if (temp.getmComplete())
					CollectOrderActivity.Start(getActivity(), temp);
				else
					Toast.makeText(getActivity().getApplicationContext(), "Order not ready!", Toast.LENGTH_SHORT).show();
			}
		});
	}

}

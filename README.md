# RecyclerPageView

用RecyclerView实现ViewPager翻页效果，页面可以是列表

### Add the dependency
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```
dependencies {
	        implementation 'com.github.Edward-yfbx:RecyclerPageView:1.0.0'
	}
```

### How to Use

使用方法与正常的RecyclerView没有区别，但需要一些特定的设置：  
1.RecyclerPageView其实是一个横向的RecyclerView,且LayoutManager是GridLayoutManager;  
2.GridLayoutManager 的 SpanCount属性决定页面中Item的数量;  
3.刷新数据后需要调用recyclerPageView.notifyDataSetChanged();而不是Adaper的notifyDataSetChanged()方法;  
  
关于1、2，RecyclerPageView中提供了一个带两个参数的setAdapter(Adapter adapter, int pageItemSize)方法，以初始化设置  
关于3，实际上如果没有分页加载和翻页监听，完全可以直接使用RecyclerView实现,比如页数固定的轮播图  



BaseRecyclerView
===================


RecyclerView를 Custom 한 것입니다. 

----------

Documents
-------------
> **RecyclerView :**
>  1. RecyclerView
  - support HeaderView and FooterView 
  - support LoadingView and More Loading


*HFERecyclerView

----------

-Header + Footer + Endless RecyclerView 의 약자

-addHeaderView , addFooterView를 이용하여 Header와 Footer를 추가 가능

-setProgressView에서 Loading에 사용될 View를 셋팅

-refreshing : Loading ViewHolder를 사용할지 안할지 여부 

-setRefreshing(boolean refreshing) : refreshing 상태 변경 

-setPager(BaseRecyclerPager pager) : 더보기 로딩시 Activity에서 추가 행동을 할 수 있는 Interface 

> BaseRecyclerPage Interface
>- 더보기 기능에 대한 interface
>
>-shouldLoad : 더보기 사용 여부
>
>-loadNextPage : 더보기 시 추가행동 셋팅 매개변수로 adapter 의 Count 

-threshold : 더보기 호출 시점

-현재 LinearLayoutManager 만 호환



*BaseRecyclerAdapter

----------

BaseRecyclerAdapter 를 상속받아 Adapter를 만들어 줍니다.

BaseRecyclerAdapter은 BaseRecyclerItem을 상속받은 Class에서 viewType에 따라 onCreateViewHolder에서 분기처리 되어 onCreateHolder Method를 호출합니다. 

사용자는 onCreateHolder에서 viewType에 따라 ViewHolder를 생성합니다.

*


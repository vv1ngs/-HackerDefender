更新记录

2020年4月14日09:58:44
重要更新：更新了表结构，新增了docker容器相关的表,更新了题目表 

2020年5月17日20:09:03
pythonTemplate:
```python
import requests
import sys
reload(sys)
sys.setdefaultencoding( "utf-8" )
str=requests.get("http://118.24.120.71:8088/static/"+sys.argv[1]).text
exec(str,{"targerUrl":sys.argv[2]})
```

2020年6月4日20:12:22
这是一个基于SSM+redis的分布式架构的安全演练平台。
基于动态docker的启动方式，你可以使用shell直接与的docker进行shell交互。
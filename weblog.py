from selenium import webdriver
from random import shuffle
browser = webdriver.Chrome("/path/to/chromedriver");

browser.get('http://localhost:8118/')

a = ["about" ,"black" ,"cassandra" ,"finance" ,"hadoop" ,"hive" ,"zookeeper" ,"mahout"]

for i in range(10000):
    shuffle(a)
    ele = browser.find_element_by_id(a[0])
    ele.click()


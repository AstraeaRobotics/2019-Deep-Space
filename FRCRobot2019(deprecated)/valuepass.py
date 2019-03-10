import keyboard
from networktables import NetworkTables
NetworkTables.initialize(server='rasppifront')
sd = NetworkTables.getTable('SmartDashboard')
print ("Please type the angle you want to send to the client")
nb = input("What's the angle?: ")
sd.putNumber('angle',nb )

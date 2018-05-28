import sys
from PyQt4 import QtGui,QtCore
import FirebaseFetch as FB
import Invoice 

class MyWidget(QtGui.QWidget):

	def __init__(self):
		super(MyWidget,self).__init__()
		self.H1 = QtGui.QFont("Times", 28, QtGui.QFont.Bold) 
		self.H2 = QtGui.QFont("Times", 16) 
		self.H3 = QtGui.QFont("Times", 12) 
		self.initUI()

	def initUI(self):
		self.w = 1366
		self.h = 690
		self.setWindowTitle("Generate Invoice")
		
		'''
		DEFINE LAYOUTS
		'''
		self.MainBox = QtGui.QGridLayout()
		


		self.HeadBox = QtGui.QHBoxLayout()
		self.HeadBox.addStretch(1)

		self.BodyBox = QtGui.QVBoxLayout()
		self.BodyBox.addStretch(1)

		self.TBox = QtGui.QVBoxLayout()
		self.TBox.addStretch(1)

		self.ContentBox = QtGui.QVBoxLayout()
		self.ContentBox.addStretch(1)
		
		self.BtnBox = QtGui.QVBoxLayout()
		self.BtnBox.addStretch(1)

		self.TotalBox = QtGui.QVBoxLayout()
		self.TotalBox.addStretch(1)

		self.WeightBox = QtGui.QVBoxLayout()
		self.WeightBox.addStretch(1)

		self.ContentBox2 = QtGui.QHBoxLayout()
		self.WeightBox.addStretch(1)
		


		'''
		ADD LAYOUTS IN LAYOUTS
		'''

		self.MainBox.addLayout(self.HeadBox,0,3)
		self.MainBox.addLayout(self.TBox,1,2)
		self.MainBox.addLayout(self.ContentBox,1,5)
		self.MainBox.addLayout(self.ContentBox2,2,3)

		self.ContentBox.addLayout(self.BtnBox)
		# self.ContentBox.addLayout(self.TotalBox)
		# self.ContentBox.addLayout(self.WeightBox)

		self.ContentBox2.addLayout(self.TotalBox)
		self.ContentBox2.addLayout(self.WeightBox)

		'''
		DEFINE WIDGETS
		'''
		self.Heading = self.addLabel("SHOP AND GO",self.H1)
		
		self.Table = self.CreateEmptyTable()

		self.TotalLbl = self.addLabel("TOTAL",self.H2)
		self.TotalVal = self.addLabel("INR 0",self.H2)
		self.WeightLbl = self.addLabel("WEIGHT",self.H2)
		self.WeightVal = self.addLabel("0 g",self.H2)
		
		self.FetchBtn = self.addButton("Fetch Cart",self.FetchCart,self.H2)
		self.CheckoutBtn = self.addButton("CheckOut",self.Checkout,self.H2)

		#ADD Widgets to Layouts
		self.HeadBox.addWidget(self.Heading, QtCore.Qt.AlignCenter)
		self.TBox.addWidget(self.Table)
		self.BtnBox.addWidget(self.FetchBtn)
		self.BtnBox.addWidget(self.CheckoutBtn)

		self.TotalBox.addWidget(self.TotalLbl)
		self.TotalBox.addWidget(self.TotalVal)
		self.WeightBox.addWidget(self.WeightLbl)
		self.WeightBox.addWidget(self.WeightVal)
		self.setLayout(self.MainBox)
		self.show()

	def addButton(self,text,command,font):
		btn = QtGui.QPushButton(text,self)
		btn.resize(btn.sizeHint())
		btn.setFont(font)
		btn.clicked.connect(command)
		return btn

	def addLabel(self,text,font):
		lbl = QtGui.QLabel(self)
		lbl.setText(text)
		lbl.setFont(font)
		lbl.adjustSize()
		return lbl

	def OTPDialog(self):
		text,ok = QtGui.QInputDialog.getText(self,"USER AUTH","Enter OTP")
		if ok:
			return text
		else:
			return False

	def FetchCart(self):
		#OTP RETRIEVE
		self.CurrentUser = None
		self.Items = None
		self.TotalAmount = None

		otp = self.OTPDialog()
		#OTP VALIDATE
		if otp:
			data,Amount,Weight,UID = FB.fetch(otp)
		
			if data:
				self.Items = data
				self.CurrentUser = UID
				self.TotalAmount = Amount
				self.ClearTBox()
				table = self.CreateTable(data)
				self.TBox.addWidget(table)
				self.TotalVal.setText("INR " + str(Amount))
				self.WeightVal.setText(str(Weight) + " g")
				self.TotalVal.adjustSize()
				self.WeightVal.adjustSize()

			else:
				table = self.CreateEmptyTable()
				self.TBox.addWidget(table)
				QtGui.QMessageBox.about(self, "OTP ERROR", "INVALID OTP")
			

	def Checkout(self):
		if self.CurrentUser:
			FB.RemoveOtp(self.CurrentUser)
			Invoice.generate(self.Items,self.TotalAmount)	
			FB.MoveCartToOrders(self.CurrentUser)
			#Clear Table For Checkout
			table = self.CreateEmptyTable()
			self.TBox.addWidget(table)		
			self.TotalVal.setText("INR 0")
			self.WeightVal.setText("0 g")
			self.TotalVal.adjustSize()
			self.WeightVal.adjustSize()

	



	def CreateTable(self,data):
		table = QtGui.QTableWidget(self)

		table.setRowCount(len(data["Product"]))
		table.setColumnCount(4)

		headers = ["Product", "Price", "Count", "Total"]
		table.setHorizontalHeaderLabels(headers)
		for n, key in enumerate(headers):
			for m,product in enumerate(data[key]):
				newItem = QtGui.QTableWidgetItem(str(product))
				table.setItem(m,n,newItem)
				table.resizeColumnsToContents()
				table.resizeRowsToContents()

		return table

	

	def CreateEmptyTable(self):
		#Remove Table From View
		self.ClearTBox()

		#Create Empty Dictionary		
		data = {
			'Product' : [],
			'Price' : [],
			'Count' : [],
			'Total' : [],
			}
		
		Table = self.CreateTable(data)
		return Table

	def ClearTBox(self):
		#Remove Table From View
		for i in reversed(range(self.TBox.count())): 
			if self.TBox.itemAt(i).widget():
				self.TBox.itemAt(i).widget().setParent(None)	




def main():
	app = QtGui.QApplication(sys.argv)
	w=MyWidget()
	app.exec_()

if __name__ == '__main__':
	main() 

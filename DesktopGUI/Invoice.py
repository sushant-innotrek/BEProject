from reportlab.pdfgen import canvas
from reportlab.lib.pagesizes import letter
from reportlab.lib.pagesizes import portrait

import FirebaseFetch as FB


def generate(data,Amount):
	c = canvas.Canvas("invoice.pdf",pagesize = portrait(letter))
	c.setFont('Helvetica',30,leading=None)
	c.drawCentredString(300,700,"WALLMART SUPERMARKET")
	
	c.setFont('Helvetica',24,leading=None)
	c.drawCentredString(300,650,"INVOICE")
	
	c.setFont('Helvetica',12,leading=None)
	c.drawCentredString(300,620,"="*80)
	c.drawString(50,600,"PRODUCT")
	c.drawString(200,600,"PRICE")
	c.drawString(350,600,"QTY")
	c.drawString(500,600,"AMT")
	c.drawCentredString(300,580,"="*80)

	posY = 580
	for x in range(len(data['Product'])):
		product = data['Product'][x]
		price = data['Price'][x]
		qty = data['Count'][x]
		amt = data['Total'][x]

		posY -= 20
		c.drawString(50,posY,str(product))
		c.drawString(200,posY,str(price))
		c.drawString(350,posY,str(qty))
		c.drawString(500,posY,str(amt))
	
	posY-=20
	c.setFont('Helvetica',12,leading=None)
	c.drawCentredString(300,posY,"="*80)
	
	posY-=20
	c.setFont('Helvetica',16,leading=None)
	c.drawString(50,posY,"TOTAL")
	c.drawString(500,posY,str(Amount))
	
	posY-=20
	c.setFont('Helvetica',12,leading=None)
	c.drawCentredString(300,posY,"="*80)

	c.showPage()
	c.save()



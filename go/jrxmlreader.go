package main

import (
	"encoding/xml"
	"io/ioutil"
	"log"
)

type JasperReport struct {
	XMLName    xml.Name    `xml:"jasperReport"`
	Fields     []Field     `xml:"field"`
	Parameters []Parameter `xml:"parameter"`
	Variables  []Variable  `xml:"variable"`
}

type Field struct {
	XMLName xml.Name `xml:"field"`
	Name    string   `xml:"name,attr"`
}

type Parameter struct {
	XMLName xml.Name `xml:"parameter"`
	Name    string   `xml:"name,attr"`
}

type Variable struct {
	XMLName xml.Name `xml:"variable"`
	Name    string   `xml:"name,attr"`
}

func ReadJasperReport(fileName string) *JasperReport {
	report := new(JasperReport)
	content, err := ioutil.ReadFile(fileName)
	if err != nil {
		log.Fatal(err)
	}
	data := string(content)
	if err := xml.Unmarshal([]byte(data), report); err != nil {
		panic(err)
	}
	return report
}

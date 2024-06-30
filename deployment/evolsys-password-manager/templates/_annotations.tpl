{{- define "app.annotation" -}}
dev/app.code: {{ .Values.app.code | quote }}
dev/app.description: {{ .Chart.Description | quote }}
dev/app.version: {{ .Values.app.version | quote }}
{{- end -}}